package locked;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import common.ExcelWriter;
import locked.base.SelfOrganizingLockList;
import locked.executorImpl.AddRemoveExecutorImpl;
import locked.executorImpl.AddRemoveSearchExecutorImpl;
import locked.executorImpl.AddSearchExecutorImpl;
import locked.executorImpl.RemoveSearchExecutorImpl;

public class RunLockedExecutor extends ExcelWriter {

	private static final int ADDS = 100;
	private static final int REMOVES = 50;
	private static final int SEARCHES = 55;
	private static final int SEARCHABLE = 10000;

	public static void main(String args[]) {

		List<HashMap<String, String>> listSearchRemove = new ArrayList<HashMap<String, String>>();
		List<HashMap<String, String>> listAddRemove = new ArrayList<HashMap<String, String>>();
		List<HashMap<String, String>> listAddSearch = new ArrayList<HashMap<String, String>>();
		List<HashMap<String, String>> listAddSearchRemove = new ArrayList<HashMap<String, String>>();

		for (int i = 0; i < 10; i++) {
			listSearchRemove
					.add(new RemoveSearchExecutorImpl(ADDS, REMOVES, SEARCHES, SEARCHABLE, new SelfOrganizingLockList())
							.removeSearch());
			listAddRemove.add(new AddRemoveExecutorImpl(ADDS, REMOVES, new SelfOrganizingLockList()).addRemove());
			listAddSearch.add(
					new AddSearchExecutorImpl(ADDS, SEARCHES, SEARCHABLE, new SelfOrganizingLockList()).addSearch());
			listAddSearchRemove.add(
					new AddRemoveSearchExecutorImpl(ADDS, REMOVES, SEARCHES, SEARCHABLE, new SelfOrganizingLockList())
							.addRemoveSearch());
		}

		writeStudentsListToExcel("LremSearch.xls", listSearchRemove);
		writeStudentsListToExcel("LaddSearch.xls", listAddSearch);
		writeStudentsListToExcel("LaddRemSearch.xls", listAddSearchRemove);
		writeStudentsListToExcel("LaddRem.xls", listAddRemove);
	}
}
