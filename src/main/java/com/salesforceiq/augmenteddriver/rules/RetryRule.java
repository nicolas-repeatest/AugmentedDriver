package com.salesforceiq.augmenteddriver.rules;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class RetryRule implements TestRule {

	private int retryCount, currentTry;

	private boolean allGood = false;

	public RetryRule(int retryCount) {
		this.retryCount = retryCount;
		this.currentTry = 1;
	}

	public boolean isLastTry() {
		return currentTry == retryCount;
	}

	public void setNotGood() {
		allGood = false;
	}

	public Statement apply(final Statement base, final Description description) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				// implement retry logic here
				for (; currentTry <= retryCount && !allGood; currentTry++) {
					allGood = true;
					System.out.println("Try #" + currentTry);
					base.evaluate();
				}
			}
		};
	}

}
