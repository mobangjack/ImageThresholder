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

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


/**
 * Image thresholding based on max entropy theory
 * <p>基于最大熵定理的图像分割算法实现
 * @author 帮杰
 *
 */
public class MaxEntropyImageThresholder extends Thresholder {

	public MaxEntropyImageThresholder(Image image) {
		super(image);
	}
	
	public MaxEntropyImageThresholder(File imageFile) throws IOException {
		super(ImageIO.read(imageFile));
	}
	
	public MaxEntropyImageThresholder(String imageFilePath) throws IOException {
		super(new File(imageFilePath));
	}
	
	@Override
	public int threshold() {
		//initial threshold
		int threshold = 0;
		//initial max entropy
		double h_max = 0;
		//normalized histogram
		double[] nh = nhistogram();
		//cumulative distribution
		double[] cd = cumulative(nh);
		for (int i = 0; i < 256; i++) {
			//low range cumulative distribution
			double cl = cd[i];
			//low range entropy
			double hl = 0;
			if (cl>0) {
				for (int j = 0; j < i+1; j++) {
					if (nh[j]>0) {
						hl = hl - (nh[j]/cl)*Math.log(nh[j]/cl);
					}
				}
			}
			//high range cumulative distribution
			double ch = 1-cl;
			//high range entropy
			double hh = 0;
			if (ch>0) {
				for (int j = i+1; j < 256; j++) {
					if (nh[j]>0) {
						hh = hh - (nh[j]/ch)*Math.log(nh[j]/ch);
					}
				}
			}
			double h = hl+hh;
			if (h>h_max) {
				//update max entropy
				h_max = h;
				//update best threshold
				threshold = i;
			}
		}
		//System.out.println("threshold="+threshold);
		//System.out.println("h_max="+h_max);
		return threshold;
	}
	
	/**
	 * 测试
	 * @param arggs
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String filePath = "G:/java/project/ImageThresholder/src/main/test2.jpg";
		Image image = ImageIO.read(new File(filePath));
		Thresholder thresholder = new MaxEntropyImageThresholder(image);
		thresholder.doThresholding();
		BufferedImage img = thresholder.generateBufferedImage();
		MyGUI ui = new MyGUI();
		ui.addImage("",image).addImage("", img);
		ui.setVisible(true);
	}
}
