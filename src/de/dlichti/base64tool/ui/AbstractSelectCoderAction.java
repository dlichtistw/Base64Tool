/*
 * This file is part of the Base64Tool project.
 * 
 * Copyright (c) 2016, David Lichti
 * All rights reserved.
 * 
 * The Base64Tool is published under the BSD-2-Clause license.
 */

package de.dlichti.base64tool.ui;

import javax.swing.AbstractAction;

import de.dlichti.base64tool.Base64Coder;

public abstract class AbstractSelectCoderAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	/**
	 * This is the encoder that the action relates to. It is never null.
	 */
	public final Base64Coder coder;
	
	public AbstractSelectCoderAction () {
		this(new Base64Coder());
	}
	public AbstractSelectCoderAction (Base64Coder coder) {
		if (coder == null) {
			throw new NullPointerException("The encoder must not be null.");
		} else {
			this.coder = coder;
		}
	}
}
