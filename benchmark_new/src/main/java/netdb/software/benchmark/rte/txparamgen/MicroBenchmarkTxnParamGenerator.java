package netdb.software.benchmark.rte.txparamgen;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import netdb.software.benchmark.TpccConstants;
import netdb.software.benchmark.TransactionType;
import netdb.software.benchmark.remote.SutConnection;
import netdb.software.benchmark.util.RandomNonRepeatGenerator;
import netdb.software.benchmark.util.RandomValueGenerator;

public class MicroBenchmarkTxnParamGenerator implements TxParamGenerator {
	private static final double WRITE_PERCENTAGE;
	private static final double CONFLICT_RATE;
	private static final int PARTITION_NUM=1;
	private static final int DATA_SIZE_PER_PART;
	private static final int HOT_DATA_SIZE_PER_PART;
	private static final int COLD_DATA_SIZE_PER_PART;
	private static final int COLD_DATA_PER_TX = 9;
	private static final int HOT_DATA_PER_TX = 1;

	private static Map<Integer, Integer> itemRandomMap;
	static {

		String prop = null;

		prop = System.getProperty(MicroBenchmarkTxnParamGenerator.class.getName()
				+ ".CONFLICT_RATE");
		CONFLICT_RATE = (prop == null ? 0.01 : Double.parseDouble(prop.trim()));

		prop = System.getProperty(MicroBenchmarkTxnParamGenerator.class.getName()
				+ ".WRITE_PERCENTAGE");
		WRITE_PERCENTAGE = (prop == null ? 0.2 : Double
				.parseDouble(prop.trim()));

		DATA_SIZE_PER_PART = TpccConstants.NUM_ITEMS / PARTITION_NUM;
		HOT_DATA_SIZE_PER_PART = (int) (1.0 / CONFLICT_RATE);
		COLD_DATA_SIZE_PER_PART = DATA_SIZE_PER_PART - HOT_DATA_SIZE_PER_PART;
		
		// initialize random item mapping map (you can shuffle the items to see if there are difference)
		itemRandomMap = new HashMap<Integer, Integer>(TpccConstants.NUM_ITEMS);
		for (int i = 1; i <= TpccConstants.NUM_ITEMS; i++)
			itemRandomMap.put(i, i);

	}



	@Override
	public TransactionType getTxnType() {
		return TransactionType.MICROBENCHMARK_TXN;
	}


	@Override
	public Object[] generateParameter() {
		RandomValueGenerator rvg = new RandomValueGenerator();
		LinkedList<Object> paramList = new LinkedList<Object>();

		// decide there is write or not
		boolean isWrite = (rvg.randomChooseFromDistribution(WRITE_PERCENTAGE,
				(1 - WRITE_PERCENTAGE)) == 0) ? true : false;
		
		// **********************
		// Start prepare params
		// **********************

		// randomly choose the main partition (currently, only 1 partition will be tested)
		int mainPartition = 0;

		int local_hot_count = HOT_DATA_PER_TX;
		int local_cold_count = COLD_DATA_PER_TX;


		int totalReadCount = local_cold_count + local_hot_count;

		paramList.add(totalReadCount);

		// randomly choose hot data
		chooseHotData(paramList, mainPartition, local_hot_count);
		// randomly choose COLD_DATA_PER_TX data from cold dataset
		chooseColdData(paramList, mainPartition, local_cold_count);

		// write
		if (isWrite) {

			totalReadCount = paramList.size() - 1;

			// set write count = read count
			paramList.add((Integer) paramList.get(0));

			// for each item been read, set their item id to be written
			for (int i = 0; i < totalReadCount; i++)
				paramList.add(paramList.get(i + 1));

			// set the update value
			for (int i = 0; i < totalReadCount; i++)
				paramList.add(rvg.nextDouble() * 100000);

		} else {
			// set write count to 0
			paramList.add(0);
		}

		return paramList.toArray();
	}

	private void chooseHotData(List<Object> paramList, int partition, int count) {
		int minMainPart = partition * DATA_SIZE_PER_PART;
		RandomNonRepeatGenerator rg = new RandomNonRepeatGenerator(
				HOT_DATA_SIZE_PER_PART);
		for (int i = 0; i < count; i++) {
			int tmp = rg.next(); // 1 ~ size
			int itemId = minMainPart + tmp;
			itemId = itemRandomMap.get(itemId);
			paramList.add(itemId);
		}

	}

	private void chooseColdData(List<Object> paramList, int partition, int count) {
		RandomNonRepeatGenerator rg = new RandomNonRepeatGenerator(
				COLD_DATA_SIZE_PER_PART);
		for (int i = 0; i < count; i++) {
			int itemId = HOT_DATA_SIZE_PER_PART + rg.next();
			itemId = itemRandomMap.get(itemId);
			paramList.add(itemId);
		}
	}
}