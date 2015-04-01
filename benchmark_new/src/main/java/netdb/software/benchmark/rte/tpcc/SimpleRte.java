package netdb.software.benchmark.rte.tpcc;

import netdb.software.benchmark.TxnResultSet;
import netdb.software.benchmark.rte.RemoteTerminalEmulator;
import netdb.software.benchmark.rte.executor.SampleTxnExecutor;
import netdb.software.benchmark.rte.executor.TransactionExecutor;
import netdb.software.benchmark.rte.txparamgen.MicroBenchmarkTxnParamGenerator;

public class SimpleRte extends RemoteTerminalEmulator {

	private MicroBenchmarkTxnParamGenerator paramGem;

	public SimpleRte(Object[] connArgs) {
		super(connArgs);
		paramGem = new MicroBenchmarkTxnParamGenerator();
	}

	@Override
	protected TxnResultSet executeTxnCycle() {
		TransactionExecutor tx = new SampleTxnExecutor(paramGem);
		return tx.execute(conn);
	}

}
