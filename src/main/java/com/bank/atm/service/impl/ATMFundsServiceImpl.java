package com.bank.atm.service.impl;

import com.bank.atm.dto.ATMDTO;
import com.bank.atm.entity.ATMFundsEntity;
import com.bank.atm.exception.ATMDataValidationException;
import com.bank.atm.repository.ATMFundsRepository;
import com.bank.atm.service.ATMFundsService;
import com.bank.atm.util.enums.CashNote;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ATMFundsServiceImpl implements ATMFundsService {

    private final ATMFundsRepository atmFundsRepository;

    public ATMFundsServiceImpl(ATMFundsRepository atmFundsRepository) {
        this.atmFundsRepository = atmFundsRepository;
    }


    @Override
    public ATMDTO availableFundsByATM(Long atmId) throws ATMDataValidationException {

        Set<ATMFundsEntity> atmEntities = atmFundsRepository.findByAtmEntity_Id(atmId);
        if (atmEntities.isEmpty()) {
            throw new ATMDataValidationException("No Cash Available in the ATM.");
        }

        //convert the availableFunds to a map and remove cash notes having zero no of cash
        Map<CashNote, Integer> availableFundsOnMap = atmEntities.stream().filter(c -> c.getNoOfCashNotes() > 0)
                .collect(Collectors.toMap(ATMFundsEntity::getCashNote, ATMFundsEntity::getNoOfCashNotes));

        //calculate the total number of funds available in the ATM
        double totalFundsAvailableInATM = availableFundsOnMap.entrySet().stream().mapToDouble(c -> c.getKey().getCashNoteValue() * c.getValue()).sum();
        ATMDTO atmDto = new ATMDTO();
        atmDto.setAtmId(atmId);
        atmDto.setRemainingBalanceInATM(BigDecimal.valueOf(totalFundsAvailableInATM));
        atmDto.setCashMap(availableFundsOnMap);
        return atmDto;
    }

    @Override
    public void deductDispensedCashFromATMCash(Long atmId, Map<CashNote, Integer> dispensedCashMap) {
        Set<ATMFundsEntity> availableATMCashList = atmFundsRepository.findByAtmEntity_Id(atmId);

        dispensedCashMap.forEach((key, value) -> {
            List<ATMFundsEntity> atmCashList = availableATMCashList.stream().filter(a -> a.getCashNote().getCashNoteValue() == key.getCashNoteValue()).collect(Collectors.toList());
            if (!atmCashList.isEmpty()) {
                atmCashList.get(0).setNoOfCashNotes(atmCashList.get(0).getNoOfCashNotes() - value);
                atmFundsRepository.save(atmCashList.get(0));
            }
        });
    }
}
