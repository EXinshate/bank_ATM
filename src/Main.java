import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ArrayList<Account> accounts = new ArrayList<>();

        System.out.println("------欢迎您来到ATM系统------");
        while (true) {
            System.out.println("1.登录账户");
            System.out.println("2.注册账户");
            System.out.println("3.请你选择操作：");
            Scanner sc = new Scanner(System.in);
            int command = sc.nextInt();
            switch (command) {
                case 1:
                    login(accounts, sc);
                    break;
                case 2:
                    register(accounts, sc);
                    break;
                default:
                    System.out.println("当前输入的操作不存在");
                    break;
            }
        }
    }

    private static void login(ArrayList<Account> accounts, Scanner sc) {
        System.out.println("------欢迎您进入到登录操作------");
        if (accounts.size() > 0) {
            while (true) {
                System.out.println("请您输入您要登录的卡号：");
                String cardID = sc.next();
                Account acc = getAccountByCardId(cardID, accounts);
                if (acc != null) {
                    while (true) {
                        System.out.println("请您输入登录的密码：");
                        String password = sc.next();
                        if (acc.getPassWord().equals(password)) {
                            System.out.println("欢迎您：" + acc.getUserName());
                            showCommand(sc, acc, accounts);
                            return;
                        } else {
                            System.out.println("您的密码不正确！");
                        }
                    }
                } else {
                    System.out.println("卡号不存在，请重新确认！");
                }
            }
        } else {
            System.out.println("当前系统无任何账户，请先注册再登录！");
        }
    }

    private static void showCommand(Scanner sc, Account acc, ArrayList<Account> accounts) {
        while (true) {
            System.out.println("------欢迎您进入操作界面------");
            System.out.println("1.查询");
            System.out.println("2.存款");
            System.out.println("3.取款");
            System.out.println("4.转账");
            System.out.println("5.修改密码");
            System.out.println("6.退出");
            System.out.println("7.注销账户");
            System.out.println("请您输入操作命令：");
            int command = sc.nextInt();
            switch (command) {
                case 1:
                    showAccount(acc);
                    break;
                case 2:
                    depositMoney(acc, sc);
                    break;
                case 3:
                    drawMoney(acc, sc);
                    break;
                case 4:
                    transferMoney(acc, accounts, sc);
                    break;
                case 5:
                    updatePassWord(acc, sc);
                    break;
                case 6:
                    System.out.println("欢迎下次继续光临！");
                    break;
                case 7:
                    accounts.remove(acc);
                    break;
                default:
                    System.out.println("您的操作有误！");
            }
        }
    }

    private static void updatePassWord(Account acc, Scanner sc) {
        while (true) {
            System.out.println("请您输入当前密码认证： ");
            String passWord = sc.next();
            if (acc.getPassWord().equals(passWord)) {
                System.out.println("请您输入新密码：");
                String newPassWord = sc.next();
                System.out.println("请您确认新密码：");
                String okPassWord = sc.next();
                if (newPassWord.equals(okPassWord)) {
                    acc.setPassWord(okPassWord);
                    System.out.println("密码已经修改成功，请重新登录！");
                    return;
                } else {
                    System.out.println("两次密码不一致！");
                }
            } else {
                System.out.println("您输入的密码有误。请重新确认密码！");
            }
        }
    }

    private static void transferMoney(Account acc, ArrayList<Account> accounts, Scanner sc) {
        if (acc.getMoney() <= 0) {
            System.out.println("您自己都没钱, 就别转了吧！");
            return;
        }
        if (accounts.size() >= 2) {
            while (true) {
                System.out.println("请您输入对方卡号: ");
                String cardId = sc.next();

                Account otherAcc = getAccountByCardId(cardId, accounts);
                if (otherAcc != null) {
                    if (acc.getCardID().equals(otherAcc.getCardID())) {
                        System.out.println("不能给自己账号转账！");
                    } else {
                        String rs = "*" + otherAcc.getUserName().substring(1);
                        System.out.println("请您确认[" + rs + "]的姓氏！");
                        System.out.println("请您输入对方的姓氏： ");
                        String prename = sc.next();
                        if (otherAcc.getUserName().startsWith(prename)) {
                            while (true) {
                                System.out.println("请您输入转账的金额（您最多可以转账: " + acc.getMoney() + "元）：");
                                double money = sc.nextDouble();
                                if (money > acc.getMoney()) {
                                    System.out.println("没有足够余额可以转！");
                                } else {
                                    acc.setMoney(acc.getMoney() - money);
                                    otherAcc.setMoney(otherAcc.getMoney() + money);
                                    System.out.println("您已经完成转账！您当前还剩余： " + acc.getMoney());
                                    return;
                                }
                            }
                        } else {
                            System.out.println("您输入对方的信息有误！");
                        }
                    }
                } else {
                    System.out.println("您输入的转账卡号不存在！");
                }
            }
        } else {
            System.out.println("当前系统中没有其他的账户可以转账，去注册一个吧！");
        }
    }

    private static void drawMoney(Account acc, Scanner sc){
        System.out.println("-------欢迎进入账户取款操作------");
        double money = acc.getMoney();
        if(money >= 100){
            while (true){
                System.out.println("请您输入取钱的金额： ");
                double drawMoney = sc.nextDouble();
                if(drawMoney > acc.getQuotaMoney()){
                    System.out.println("您当前取款金额超过了每次限额");
                }else {
                    if(drawMoney > money){
                        System.out.println("当前余额不足！ 当前的余额是： " + money);
                    }else {
                        acc.setMoney(money - drawMoney);
                        System.out.println("您当前取钱完成，请拿出你的钱，当前余额是： " + acc.getMoney());
                        break;
                    }
                }
            }
        }else {
            System.out.println("您当前余额不足100元,无法取出！");
        }
    }

    private static void depositMoney(Account acc, Scanner sc){
        System.out.println("------欢迎进入到存款操作------");
        System.out.println("请您输入存款金额：");
        double money = sc.nextDouble();
        acc.setMoney(acc.getMoney() + money);
        showAccount(acc);
    }
    private static void showAccount(Account acc){
        System.out.println("------您当前账户详情信息如下------");
        System.out.println("卡号： " + acc.getCardID());
        System.out.println("户主： " + acc.getUserName());
        System.out.println("余额： " + acc.getMoney());
        System.out.println("当次取现额度： " + acc.getQuotaMoney());
    }

    private static void register(ArrayList<Account> accounts, Scanner sc){
        System.out.println("------欢迎您进入到开户操作------");
        Account acc = new Account();
        System.out.println("请您输入账户名称： ");
        String userName = sc.next();
        acc.setUserName(userName);

        while (true){
            System.out.println("请您输入账户密码：");
            String passWord = sc.next();
            System.out.println("请您输入确认密码：");
            String okPassWord = sc.next();
            if (okPassWord.equals(passWord)){
                acc.setPassWord(okPassWord);
                break;
            }else {
                System.out.println("两次输入的密码不一致!");
            }
        }
        System.out.println("请您设置当次取现额度：");
        double quataMoney = sc.nextDouble();
        acc.setQuotaMoney(quataMoney);
        String cardId = createCardId(accounts);
        acc.setCardID(cardId);

        accounts.add(acc);
        System.out.println("恭喜您，" + acc.getUserName() +
                "先生/女生, 您的卡号是：" + acc.getCardID());
      }

      public static String createCardId(ArrayList<Account> accounts){
        while (true) {
            String cardId = "";
            Random r = new Random();
            for (int i = 1; i <= 8; i++) {
                cardId += r.nextInt(10);
            }
            Account account = getAccountByCardId(cardId, accounts);
            if (account == null){
                return cardId;
            }
        }
      }
      public static Account getAccountByCardId(String cardId, ArrayList<Account> accounts) {
          for (int i = 0; i < accounts.size(); i++) {
              Account acc = accounts.get(i);
              if (acc.getCardID().equals(cardId)){
                  return acc;
              }
          }
          return null;
      }
}