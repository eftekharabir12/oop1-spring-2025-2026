interface IMemberOperation {
    double discountedFee();

    default void display() {
        System.out.println("Member operation applied.");
    }
}

abstract class Member {

    private String memberID;
    private double monthlyFee;

    Member() {}

    Member(String memberID, double monthlyFee) {
        System.out.println("Member constructor called");
        this.memberID = memberID;
        this.monthlyFee = monthlyFee;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public double getMonthlyFee() {
        return monthlyFee;
    }

    public void setMonthlyFee(double monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    abstract void showInfo();
}

class PlatinumMember extends Member implements IMemberOperation {

    private int freeSessions;

    PlatinumMember() {}

    PlatinumMember(String memberID, double monthlyFee, int freeSessions) {
        super(memberID, monthlyFee);
        System.out.println("PlatinumMember constructor called");
        this.freeSessions = freeSessions;
    }

    public double discountedFee() {
        if (getMonthlyFee() > 8000) {
            return getMonthlyFee() * 0.92;
        }
        return getMonthlyFee();
    }

    void showInfo() {
        System.out.println("Platinum Member ID: " + getMemberID());
        System.out.println("Fee: " + getMonthlyFee());
        System.out.println("Discounted Fee: " + discountedFee());
        System.out.println("Free Sessions: " + freeSessions);
        System.out.println();
    }
}

class StandardMember extends Member implements IMemberOperation {

    private boolean groupClassAccess;

    StandardMember() {}

    StandardMember(String memberID, double monthlyFee, boolean groupClassAccess) {
        super(memberID, monthlyFee);
        System.out.println("StandardMember constructor called");
        this.groupClassAccess = groupClassAccess;
    }

    public double discountedFee() {
        if (getMonthlyFee() > 8000) {
            return getMonthlyFee() * 0.92;
        }
        return getMonthlyFee();
    }

    void showInfo() {
        System.out.println("Standard Member ID: " + getMemberID());
        System.out.println("Fee: " + getMonthlyFee());
        System.out.println("Discounted Fee: " + discountedFee());
        System.out.println("Group Class Access: " + groupClassAccess);
        System.out.println();
    }
}

class Gym {

    private String name;
    private Member[] members;
    private int count = 0;

    Gym() {}

    Gym(String name, int size) {
        System.out.println("Gym constructor called");
        this.name = name;
        members = new Member[size];
    }

    void addMember(Member m) {
        if (count < members.length) {
            members[count++] = m;
        }
    }

    void removeMember(String id) {
        for (int i = 0; i < count; i++) {
            if (members[i].getMemberID().equals(id)) {
                members[i] = members[count - 1];
                members[count - 1] = null;
                count--;
                break;
            }
        }
    }

    void showMembers() {
        System.out.println("Gym Name: " + name);
        for (int i = 0; i < count; i++) {
            members[i].showInfo();
        }
    }

    void totalRevenue() {
        double sum = 0;

        for (int i = 0; i < count; i++) {
            IMemberOperation op = (IMemberOperation) members[i];
            sum += op.discountedFee();
        }

        System.out.println("Total Revenue: " + sum);
    }
}

public class Start {

    public static void main(String[] args) {

        Member m1 = new PlatinumMember("P101", 20000, 5);
        Member m2 = new StandardMember("S101", 10S000, true);

        Gym g = new Gym("FitZone", 5);

        g.addMember(m1);
        g.addMember(m2);

        g.showMembers();

        g.totalRevenue();

        ((IMemberOperation)m1).display();

        g.removeMember("S101");

        System.out.println("After removal:");
        g.showMembers();
    }
}