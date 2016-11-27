/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.model;

public enum BuildResult {
    FAILURE, UNSTABLE, REBUILDING, BUILDING, ABORTED, SUCCESS, UNKNOWN, NOT_BUILT, 
    /**
     * This will be the result of a job in cases where it has been 
     * cancelled during the time in the queue. 
     */
    CANCELLED
}
