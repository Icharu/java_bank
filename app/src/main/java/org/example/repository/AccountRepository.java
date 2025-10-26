package org.example.repository;

import java.util.List;

import org.example.model.AccountWallet;

public class AccountRepository {

    private List<AccountWallet> accounts;

    public AccountWallet findByPix(final String pix) {
        return accounts.stream()
                       .filter(a -> a.getPix().contains(pix))
                       .findFirst()
                       .orElseThrow(() -> new WalletNotFoundException("Conta com o pix " + pix + " não encontrada"));
    }

    public AccountWallet create(final List<String> pix, final long initialFunds) {
        var newAccount = new AccountWallet(initialFunds, pix);
        this.accounts.add(newAccount);
        return newAccount;
    }

    public void deposit(final String pix, final long fundsAmount) {
        var target = findByPix(pix);
        target.addMoney(fundsAmount, "Depósito realizado na conta");
    }

    public long withdraw(final String pix, final long amount) {
        var source = findByPix(pix);
        checkFundsForTransaction(source, amount);
        source.reduceMoney(amount);
        return amount;
    }
    public void transferMoney(final String sourcePix, final String targetPix, final long amount) {
        var source = findByPix(sourcePix);
        var target = findByPix(targetPix);
        checkFundsForTransaction(source, amount);
        var message = "Transferência realizada da conta " + sourcePix + " para a conta " + targetPix;
        target.addMoney(source.reduceMoney(amount), source.getService(), message);
    }
    

    public List<AccountWallet> list(){
        return this.accounts;
    }

}
