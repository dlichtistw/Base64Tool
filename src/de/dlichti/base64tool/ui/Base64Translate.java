/*
 * This file is part of the Base64Tool project.
 * 
 * Copyright (c) 2016, David Lichti
 * All rights reserved.
 * 
 * The Base64Tool is published under the BSD-2-Clause license.
 */

package de.dlichti.base64tool.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JRadioButton;

import de.dlichti.base64tool.Base64Coder;
import de.dlichti.base64tool.Base64CoderMIME;
import de.dlichti.base64tool.Base64Encoding;
import de.dlichti.base64tool.Base64Exception;
import de.dlichti.base64tool.Radix64Coder;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.ButtonGroup;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Base64Translate {
	// Some important UI components
	private JFrame mainFrame;
	private final ButtonGroup encodingButtonGroup = new ButtonGroup();
	private final InputArea txtrClearText = new InputArea("Clear Text");
	private final InputArea txtrEncodedText = new InputArea("Encoded Text");
	
	private JRadioButton defaultButton;
	
	// Actions to change encoding
	private final SelectCoderAction selectBase64Action = new SelectCoderAction(new Base64Coder());
	private final SelectCoderAction selectBase64UrlAction = new SelectCoderAction(new Base64Coder(Base64Encoding.BASE_64_URL));
	private final SelectCoderAction selectBase64XmlAction = new SelectCoderAction(new Base64Coder(Base64Encoding.BASE_64_XML_NAME));
	private final SelectCoderAction selectBase64MIMEAction = new SelectCoderAction(new Base64CoderMIME());
	private final SelectCoderAction selectRadix64Action = new SelectCoderAction(new Radix64Coder());
	
	/**
	 * The currently enabled encoder class
	 */
	protected Base64Coder encoder;
	
	/**
	 * Reflects the current usage mode.
	 * Set {@code true} if the program should encode clear text.
	 * Set {@code false} if the program should decode encoded text.
	 */
	protected boolean encode;

	/**
	 * Launch the application.
	 */
	public static void main (String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run () {
				try {
					Base64Translate window = new Base64Translate();
					window.mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Base64Translate () {
		initialize();
		
		this.encode = true;
		this.encodingButtonGroup.setSelected(this.defaultButton.getModel(), true);
		this.selectBase64Action.actionPerformed(null);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize () {
		mainFrame = new JFrame();
		mainFrame.setTitle("Base64Code");
		mainFrame.setBounds(100, 100, 507, 376);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel optionsPanel = new JPanel();
		mainFrame.getContentPane().add(optionsPanel, BorderLayout.NORTH);
		GridBagLayout gbl_optionsPanel = new GridBagLayout();
		gbl_optionsPanel.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gbl_optionsPanel.rowHeights = new int[]{0, 0};
		gbl_optionsPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_optionsPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		optionsPanel.setLayout(gbl_optionsPanel);
		
		int gridx = 0;
		defaultButton = addEncodingButton(selectBase64Action, optionsPanel, gridx++, 0);
		addEncodingButton(selectBase64MIMEAction, optionsPanel, gridx++, 0);
		addEncodingButton(selectRadix64Action, optionsPanel, gridx++, 0);
		addEncodingButton(selectBase64UrlAction, optionsPanel, gridx++, 0);
		addEncodingButton(selectBase64XmlAction, optionsPanel, gridx++, 0);
		
		JPanel contentPanel = new JPanel();
		mainFrame.getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		this.txtrClearText.setWrapStyleWord(true);
		contentPanel.add(this.txtrClearText);
		this.txtrClearText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				Base64Translate.this.encode = true;
				Base64Translate.this.updateText();
			}
		});
		
		contentPanel.add(this.txtrEncodedText);
		txtrEncodedText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				Base64Translate.this.encode = false;
				Base64Translate.this.updateText();
			}
		});
	}
	
	protected JRadioButton addEncodingButton (SelectCoderAction action, JPanel panel, int gridx, int gridy) {
		JRadioButton button = new JRadioButton();
		button.setAction(action);
		encodingButtonGroup.add(button);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(0, 0, 0, 5);
		constraints.gridx = gridx;
		constraints.gridy = gridy;
		panel.add(button, constraints);
		return button;
	}

	protected void updateText () {
		System.out.println("Updating text field contents");
		
		if (this.encode) this.updateEncoded();
		else this.updateDecoded();
	}
	private void updateEncoded () {
		final String clearText = this.txtrClearText.getText();
		final String encodedText = this.encoder.encode(clearText.getBytes());					
		this.txtrEncodedText.setText(encodedText);
		this.txtrEncodedText.setState(InputArea.VALID);
	}
	private void updateDecoded () {
		final String encodedText = this.txtrEncodedText.getText();
		try {
			String clearText = new String(this.encoder.decode(encodedText));
			this.txtrClearText.setText(clearText);
			this.txtrEncodedText.setState(InputArea.VALID);
		} catch (Base64Exception e) {
			e.printStackTrace();
			
			this.txtrClearText.setText(e.getMessage());
			this.txtrEncodedText.setState(InputArea.INVALID);
		}					
	}
	
	private class SelectCoderAction extends AbstractSelectCoderAction {
		private static final long serialVersionUID = 1L;
		
		public SelectCoderAction (Base64Coder coder) {
			super (coder);
			
			putValue(NAME, this.coder.getName());
			putValue(SHORT_DESCRIPTION, String.format("Use %s encoding", this.coder.getName()));
		}
		
		public void actionPerformed(ActionEvent e) {
			Base64Translate.this.encoder = this.coder;
			System.out.println(String.format("Set encoding to %s.", this.coder.getName()));
			Base64Translate.this.updateText();
		}
	}
}
