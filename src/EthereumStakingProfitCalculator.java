import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class EthereumStakingProfitCalculator {

    public static void main(String[] args) throws IOException {

        BigDecimal initialInvestment = new BigDecimal("25");
        BigDecimal yearlyStakingRewardRate = new BigDecimal("0.1");
        LocalDate stakingStartDate = LocalDate.of(2024, 4, 15);
        int stakingDurationInMonths = 24;
        int rewardPaymentDay = 23;
        boolean reinvestRewards = true;

        calculateAndGenerateProfitSchedule(
                initialInvestment,
                yearlyStakingRewardRate,
                stakingStartDate,
                stakingDurationInMonths,
                rewardPaymentDay,
                reinvestRewards);
    }

    public static void calculateAndGenerateProfitSchedule(
            BigDecimal initialInvestment,
            BigDecimal yearlyStakingRewardRate,
            LocalDate stakingStartDate,
            int stakingDurationInMonths,
            int rewardPaymentDay,
            boolean reinvestRewards)
            throws IOException {


        PrintWriter writer = new PrintWriter(new FileWriter("profit_schedule.csv"));
        writer.println("Line #,Reward Date,Investment Amount,Reward Amount,Total Reward Amount till that date,Staking Reward Rate");


        BigDecimal totalRewardAmount = BigDecimal.ZERO;
        BigDecimal currentInvestment = initialInvestment;
        int line = 1;

        // Calculate and write the profit schedule
        for (int i = 0; i < stakingDurationInMonths; i++) {
            LocalDate rewardDate = stakingStartDate.plusMonths(i).withDayOfMonth(rewardPaymentDay);
            BigDecimal daysInMonth = BigDecimal.valueOf(ChronoUnit.DAYS.between(rewardDate.minusMonths(1).withDayOfMonth(rewardPaymentDay), rewardDate));
            BigDecimal rewardAmount;

            // Setting reward amount to 0 for the first month
            if (i == 0) {
                rewardAmount = BigDecimal.ZERO;
            } else {
                rewardAmount = currentInvestment
                        .multiply(yearlyStakingRewardRate)
                        .multiply(daysInMonth)
                        .divide(BigDecimal.valueOf(365.0), 6, RoundingMode.HALF_UP);
            }

            totalRewardAmount = totalRewardAmount.add(rewardAmount);

            // Update the current investment amount after calculating the reward amount
            if (reinvestRewards ) {
                currentInvestment = currentInvestment.add(rewardAmount);
            }

            writer.println(
                    line + "," +
                    rewardDate.format(DateTimeFormatter.ISO_DATE) + "," +
                    String.format("%.6f", currentInvestment) + "," +
                    String.format("%.6f", rewardAmount) + "," +
                    String.format("%.6f", totalRewardAmount) + "," +
                    String.format("%.2f", yearlyStakingRewardRate.multiply(BigDecimal.valueOf(100))) + "%"
            );

            line++;
        }

        writer.close();
    }
}