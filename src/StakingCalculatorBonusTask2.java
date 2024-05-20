import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class StakingCalculatorBonusTask2 {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);


        System.out.print("Enter initial Ethereum investment amount (Integer): ");
        BigDecimal initialInvestment = scanner.nextBigDecimal();
        scanner.nextLine();

        System.out.print("Enter staking start date (YYYY-MM-DD): ");
        LocalDate stakingStartDate = LocalDate.parse(scanner.nextLine());

        System.out.print("Enter yearly staking reward rate on staking start date (as a decimal, e.g., 0.10 for 10%): ");
        BigDecimal initialRewardRate = scanner.nextBigDecimal();
        scanner.nextLine();

        System.out.print("Enter staking duration in months (Integer): ");
        int stakingDurationInMonths = scanner.nextInt();

        System.out.print("Enter reward payment day 1-28 (Integer): ");
        int rewardPaymentDay = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Reinvest rewards (true/false): ");
        boolean reinvestRewards = scanner.nextBoolean();
        scanner.nextLine();

        System.out.print("Enter the date when the reward rate changes (YYYY-MM-DD): ");
        LocalDate rateChangeDate = LocalDate.parse(scanner.nextLine());

        System.out.print("Enter the new yearly staking reward rate after the rate change date (as a decimal, e.g., 0.08 for 8%): ");
        BigDecimal newRewardRate = scanner.nextBigDecimal();
        scanner.nextLine();

        scanner.close();

        calculateAndGenerateProfitSchedule(
                initialInvestment,
                stakingStartDate,
                stakingDurationInMonths,
                rewardPaymentDay,
                reinvestRewards,
                initialRewardRate,
                rateChangeDate,
                newRewardRate);
    }

    public static void calculateAndGenerateProfitSchedule(
            BigDecimal initialInvestment,
            LocalDate stakingStartDate,
            int stakingDurationInMonths,
            int rewardPaymentDay,
            boolean reinvestRewards,
            BigDecimal initialRewardRate,
            LocalDate rateChangeDate,
            BigDecimal newRewardRate)
            throws IOException {


        PrintWriter writer = new PrintWriter(new FileWriter("profit_schedule_bonus2.csv"));
        writer.println("Line #,Reward Date,Investment Amount,Reward Amount,Total Reward Amount till that date,Staking Reward Rate");


        BigDecimal totalRewardAmount = BigDecimal.ZERO;
        BigDecimal currentInvestment = initialInvestment;
        int line = 1;


        for (int i = 0; i < stakingDurationInMonths; i++) {
            LocalDate rewardDate = stakingStartDate.plusMonths(i).withDayOfMonth(rewardPaymentDay);
            BigDecimal daysInMonth = BigDecimal.valueOf(ChronoUnit.DAYS.between(rewardDate.minusMonths(1).withDayOfMonth(rewardPaymentDay), rewardDate));
            BigDecimal rewardAmount;


            BigDecimal yearlyStakingRewardRate = determineYearlyStakingRewardRate(rewardDate, rateChangeDate, initialRewardRate, newRewardRate);


            if (i == 0) {
                rewardAmount = BigDecimal.ZERO;
            } else {
                rewardAmount = currentInvestment
                        .multiply(yearlyStakingRewardRate)
                        .multiply(daysInMonth)
                        .divide(BigDecimal.valueOf(365.0), 6, RoundingMode.HALF_UP);
            }

            totalRewardAmount = totalRewardAmount.add(rewardAmount);


            if (reinvestRewards) {
                currentInvestment = currentInvestment.add(rewardAmount);
            }


            writer.println(
                    line + "," +
                    rewardDate.format(DateTimeFormatter.ISO_DATE) + "," +
                    String.format("%.6f", currentInvestment) + "," +
                    String.format("%.6f", rewardAmount) + "," +
                    String.format("%.6f", totalRewardAmount) + "," +
                    String.format("%.2f", yearlyStakingRewardRate.multiply(BigDecimal.valueOf(100))) + "%");

            line++;
        }

        writer.close();
    }

    private static BigDecimal determineYearlyStakingRewardRate(LocalDate date, LocalDate rateChangeDate, BigDecimal initialRewardRate, BigDecimal newRewardRate) {
        // Checks if the date is on or after the rate change date if not as a backup returns to initialRewardDate.
        if (date.isAfter(rateChangeDate) || date.isEqual(rateChangeDate)) {
            return newRewardRate;
        } else {
            return initialRewardRate;
        }
    }
}
