/*
 * This file is part of the Base64Tool project.
 * 
 * Copyright (c) 2016, David Lichti
 * All rights reserved.
 * 
 * The Base64Tool is published under the BSD-2-Clause license.
 */

package de.dlichti.base64tool.ui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

public class InputArea extends JTextArea {
	private static final long serialVersionUID = 1L;
	public static final InputAreaState VALID = InputAreaState.VALID;
	public static final InputAreaState INVALID = InputAreaState.INVALID;

	public final String title;
	protected InputAreaState state = InputAreaState.VALID;
	
	public InputArea (String title) {
		super ();
		
		this.title = title;
		this.setBorder(new TitledBorder(null, this.title, TitledBorder.LEADING, TitledBorder.TOP, null, null));
		this.setLineWrap(true);
		this.setFont(new Font("Monospaced", Font.PLAIN, 13));
	}
	
	public void setState (InputAreaState state) {
		switch (state) {
		case VALID:
			this.setBackground(Color.WHITE);
			break;
		case INVALID:
			this.setBackground(new Color(255, 127, 127));
			break;
		}
		this.state = state;
	}
	
	protected enum InputAreaState {
		VALID,
		INVALID;
	}
}
