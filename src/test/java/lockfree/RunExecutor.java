package lockfree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import common.ExcelWriter;
import lockfree.executorImpl.AddSearchExecutorImpl;

public class RunExecutor extends ExcelWriter {

	/*
	 * addRemoveSearch addRemove addSearch removeSearch
	 */

	private static final int ADDS = 2000;
	private static final int REMOVES = 500;
	private static final int SEARCHES = 550;
	private static final int SEARCHABLE = 10000;

	public static void main(String args[]) {

		List<HashMap<String, String>> listSearchRemove = new ArrayList<HashMap<String, String>>();
		List<HashMap<String, String>> listAddRemove = new ArrayList<HashMap<String, String>>();
		List<HashMap<String, String>> listAddSearch = new ArrayList<HashMap<String, String>>();
		List<HashMap<String, String>> listAddSearchRemove = new ArrayList<HashMap<String, String>>();

		for (int i = 0; i < 5; i++) {
			// listSearchRemove.add(new RemoveSearchExecutorImpl(ADDS, REMOVES,
			// SEARCHES, SEARCHABLE).removeSearch());
			// listAddRemove.add(new AddRemoveExecutorImpl(ADDS,
			// REMOVES).addRemove());
			listAddSearch.add(new AddSearchExecutorImpl(ADDS, SEARCHES, SEARCHABLE).addSearch());
			// listAddSearchRemove
			// .add(new AddRemoveSearchExecutorImpl(ADDS, REMOVES, SEARCHES,
			// SEARCHABLE).addRemoveSearch());
		}

		// writeStudentsListToExcel("remSearch.xls", listSearchRemove);
		writeStudentsListToExcel("addSearch.xls", listAddSearch);
		// writeStudentsListToExcel("addRemSearch.xls", listAddSearchRemove);
		// writeStudentsListToExcel("addRem.xls", listAddRemove);
	}

}
