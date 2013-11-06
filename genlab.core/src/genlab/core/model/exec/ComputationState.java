package genlab.core.model.exec;

public enum ComputationState {

	/**
	 * The task was just created
	 */
	CREATED,
	
	/**
	 * The task is still waiting for data or another task
	 */
	WAITING_DEPENDENCY,
	
	/**
	 * The task is ready and can be run.
	 */
	READY,
	
	/**
	 * The task was started and is running.
	 */
	STARTED,
	
	FINISHED_CANCEL,
	
	FINISHED_OK,
	
	FINISHED_FAILURE;
	
	
	/**
	 * Returns true if this state indicates the computation stopped.
	 * @return
	 */
	public boolean isFinished() {
		return this == FINISHED_OK || this == FINISHED_FAILURE || this == FINISHED_CANCEL;
	}
}
