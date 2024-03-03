package Entities;

public class DebtCategory {
    private String NameDebt;
    private String oldNameDebt;

    public DebtCategory() {
    }

    public DebtCategory(String nameDebt) {
        NameDebt = nameDebt;
        oldNameDebt=nameDebt;
    }

    public String getNameDebt() {
        return NameDebt;
    }

    public void setNameDebt(String nameDebt) {
        NameDebt = nameDebt;
    }

    public String getOldNameDebt() {
        return oldNameDebt;
    }

    public void setOldNameDebt(String oldNameDebt) {
        this.oldNameDebt = oldNameDebt;
    }

    @Override
    public String toString() {
        return  NameDebt;
    }
}
