package org.example.repository;

import java.util.ArrayList;
import java.util.List;

import org.example.exception.InvestmentNotFoundException;
import org.example.model.Investment;
import org.example.model.InvestmentWallet;
import org.example.model.AccountWallet;
import org.example.exception.WalletNotFoundException;
import org.example.exception.PixAlreadyInUseException;

public class InvestmentRepository {
    
    private long nextId;
    private final List<Investment> investments = new ArrayList<>();
    private final List<InvestmentWallet> wallets = new ArrayList<>();

    public InvestmentRepository create(final long tax, final long initialFunds) {
        this.nextId++;
        var investment = new Investment(this.nextId, tax, daysToRescue, initialFunds);
        investments.add(investment);
        return investment;
    }
    public InvestmentRepository initInvestment(final AccountWallet account, final long id) {
        var accountsInUse = wallets.stream()
                .map(InvestmentWallet::getAccount)
                .toList();
                if(accountsInUse.contains(account)) {
                    throw new PixAlreadyInUseException("O Pix " + account.getPix() + " está em uso");
                }
        var investment = findById(id);
        checkFundsForTransaction(account, investment.initialFunds());
        var wallet = new InvestmentWallet(investment, account, investment.initialFunds());
        wallets.add(wallet);
        return wallet;
    }

    public InvestmentWallet findWalletByAccountPix(final String pix) {

        return wallets.stream()
                .filter(wallet -> wallet.getAccountPix().contains(pix))
                .findFirst()
                .orElseThrow(() -> new WalletNotFoundException("Carteira de investimento não encontrada para o pix: " + pix));

    }

    public InvestmentWallet deposit(final String pix) {
        var wallet = findWalletByAccountPix(pix);
        wallet.addMoney(wallet.getAccount().reduceMoney(funds), wallet.getService(), "Aplicação de investimento realizada a partir da conta vinculada");
        return wallet;
    }

    public InvestmentWallet withdraw(final String pix) {
        var wallet = findWalletByAccountPix(pix);
        checkFundsForTransaction(wallet, funds);
        wallet.getAccount().addMoney(wallet.reduceMoney(funds), wallet.getService(), "Resgate de investimento realizado para a conta vinculada");
        if (wallet.getFunds() == 0) {
            wallets.remove(wallet);
        }
        return wallet;
    }

    public void updateAmount(final long percent) {
        wallets.forEach(wallet -> wallet.updateInvestmentValue(percent));
    }
    public InvestmentWallet findById(final long id) {
        return wallets.stream()
                .filter(wallet -> wallet.getId() == id)
                .findFirst()
                .orElseThrow(() -> new InvestmentNotFoundException("Carteira de investimento com id " + id + " não encontrada"));
    }

    public List<InvestmentWallet> listWallets() {
        return this.wallets;
    }
}
