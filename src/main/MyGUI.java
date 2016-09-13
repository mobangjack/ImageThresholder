/**
 * Copyright (c) 2011-2015, Mobangjack 莫帮杰 (mobangjack@foxmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package main;

import java.awt.FlowLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * @author 帮杰
 *
 */
public class MyGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	public MyGUI() {
		setLayout(new FlowLayout());
		setBounds(100, 100, 700, 540);
	}

	public MyGUI(int w,int h) {
		setLayout(new FlowLayout());
		setBounds(100, 100, w, h);
	}
	
	public MyGUI size(int w,int h) {
		setBounds(100, 100, w, h);
		return this;
	}
	
	public MyGUI addImage(String title,Image image) {
		add(new JLabel(title,new ImageIcon(image), 0));
		return this;
	}
	
	public MyGUI addImage(Image image) {
		add(new JLabel(new ImageIcon(image), 0));
		return this;
	}
}
