1. Locate the src folder.
2. Select desired task.
3. Locate main method and run it.


[EthereumStakingProfitCalculator] 
A straight-forward calculator with preexisting values.
I had a dilemma about whether I should display 8 days worth of staking in the 'Total Reward till that date' in the csv file
(line 55). Further calculations take it into consideration.
for a monthly reward amount's calculations I used the formula: currentInvestment * yearlyStakingRewardRate * daysInMonth /365
(Actual day amount taken from ChronoUnit class)
I had to learn using a Writer Class and tried using BigDecimal instead of double for precision.

[StakingCalculatorBonusTask1]
It was almost the same as the first task, except added additional method (line 89) to evaluate if the date is on or after April 15, 2025.
If so, it returns a new reward rate of 8%; otherwise, it returns an initial yearly rate of 10%.

[StakingCalculatorBonusTask2]
Pretty much the same except added a scanner to let the user decide what values to put in.
And a new method (line 120) that checks if the date is on or after the rate change date;
if not as backup returns to the initial reward date.

Values used: 
Enter initial Ethereum investment amount (Integer): 10
Enter staking start date (YYYY-MM-DD): 2000-10-27
Enter yearly staking reward rate on staking start date (as a decimal, e.g., 0.10 for 10%): 0.15
Enter staking duration in months (Integer): 36
Enter reward payment day 1-28 (Integer): 15
Reinvest rewards (true/false): true
Enter the date when the reward rate changes (YYYY-MM-DD): 2001-12-27
Enter the new yearly staking reward rate after the rate change date (as a decimal, e.g., 0.08 for 8%): 0.05

(10; 2000-10-27; 0.15; 36; 15; true; 2001-12-27; 0.05)
