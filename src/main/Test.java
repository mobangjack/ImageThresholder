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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author 帮杰
 *
 */
public class Test {

	private String filePath = "G:/java/project/ImageThresholder/src/main/me.jpg";
	private BufferedImage image;
	
	public Test() throws IOException {
		image = ImageIO.read(new File(filePath));
	}

	public void thresholding(Thresholder thresholder) {
		thresholder.doThresholding();
		BufferedImage img = thresholder.generateBufferedImage();
		MyGUI ui = new MyGUI();
		ui.size(img.getWidth()*2+40, img.getHeight()+50);
		ui.setTitle(thresholder.getClass().getSimpleName()+"-Threshold:"+thresholder.getThreshold());
		ui.addImage(image).addImage(img);
		ui.setVisible(true);
	}
	
	public void histogramValleyPointThresholding() {
		thresholding(new HistogramValleyPointImageThresholder(image));
	}

	public void greyExpThresholding() {
		thresholding(new GreyExpImageThresholder(image));
	}
	
	public void maxEntropyThresholding() {
		thresholding(new MaxEntropyImageThresholder(image));
	}

	public void OSTUThresholding() {
		thresholding(new OSTUImageThresholder(image));
	}

	/**
	 * 测试
	 * @param arggs
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		Test test = new Test();
		test.histogramValleyPointThresholding();
		test.greyExpThresholding();
		test.maxEntropyThresholding();
		test.OSTUThresholding();
	}
}
