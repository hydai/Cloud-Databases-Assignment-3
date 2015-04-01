# Assignment 3
In this assignment, you are asked to optimize file and buffer modules of VanillaCore.

## Steps
To complete this assignment, you need to

1. Fork the Assignment 3 project
2. Trace the code in the `vanilladb-core-patch` project's `org.vanilladb.core.storage.file` and `org.vanilladb.core.storage.buffer` package yourself
3. Modify the current implementation to reach better performance
4. Test your optimization by running some experiments
5. Write an experiment analysis report
6. Push your repository to Gitlab and open a merge request

## Optimization

The current File and Buffer Manager of VanillaCore is slow, mainly due to the synchronization for thread-safety. The main objective of this assignment is to optimize these two modules for better performance.

You can do anything you want to them, but you aslo need to **keep the correctness** of this system at the same time.

You are required to come out at least one optimization for each module
- `org.vanilladb.core.storage.file`
- `org.vanilladb.core.storage.buffer`


## Experiments

We provide you a micro-benchmark to measure your performance.

There are some parameters in benchmark you can adjust:
- NUM_RTES (value > 1) - The number of clients
- CONFLICT_RATE (1.0 > value > 0.0) - The probability of data conclict
- WRITE_PERCENTAGE (1.0 >= value >= 0.0) - The ratio of update command in a transaction


There is also a configuration in VanillaCore you can try:
- BUFFER_POOL_SIZE (value > 1) - The size of buffer pool

You need to run **at least 3 different parameter combinations** for the current implementation and your optimized version. Then, explain the results of them in the report.


## The report

- Explain what you exactly do for optimization
- Run a few experiments with the micro-benchmark
  - Show the throughput and latency between those implementation
  - Explain the result of experiments
- Discuss why your optimization works

	Note: There is no strict limitation to the length of your report. Generally, a 2~3 pages report with some figures and tables is fine. **Remember to include all the group members' student IDs in your report.**



## Submission

The procedure of submission is as following:

1. Fork our [Assignment 3](http://shwu10.cs.nthu.edu.tw/2015-cloud-database/assignment-3) on GitLab
2. Clone the repository you forked
3. Finish your work and write the report
4. Commit your work, push to GitLab and then open a merge request to submit. The repository should contain
	- *[Project directory]*
	- *[Team Member 1 ID]_[Team Member 2 ID]*_assignment3_report.pdf (e.g. 102062563_103062528_assignment3_reprot.pdf)

    Note: Each team only need one submission.

## Demo

Due to the complexity of this assignment, we hope you can come to explain your work face to face. We will announce a demo table after submission. Don't forget to choose the demo time for your team.

## Hints

Critical sections are usually used to protect some shared resource
- Reducing the size of critical sections usually makes transaction have less chance to block each others
- Some kinds of transactions will be stalled during execution due to some critical sections, even if they do not need to use those resource

## Deadline
Sumbit your work before **2015/04/15 (Wed.) 23:59:59**.
