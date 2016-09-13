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
 * 基于灰度期望值的图像分割算法
 * <br>在对随机变量的统计过程中,
 * 期望值是一个十分重要的统计特征,它反映了随机变量的平均取值，
 * 类似于物体的质量中心，因此从灰度“中心”进行分割应当是最佳的分割平衡点。
 * 该方法基于全局，分割效果要优于灰度差直方图法、微分直方图法
 * 以及非等同熵法，并且该算法的复杂度较低且处理速度较快。
 * @author 帮杰
 *
 */
public class GreyExpImageThresholder extends Thresholder {

	/**
	 * @param image
	 */
	public GreyExpImageThresholder(Image image) {
		super(image);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param imageFile
	 * @throws IOException
	 */
	public GreyExpImageThresholder(File imageFile) throws IOException {
		super(imageFile);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param imageFilePath
	 * @throws IOException
	 */
	public GreyExpImageThresholder(String imageFilePath) throws IOException {
		super(imageFilePath);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see main.Thresholder#threshold()
	 */
	@Override
	public int threshold() {
		double threshold = 0;
		double[] nhistogram = nhistogram();
		for (int i = 0; i < nhistogram.length; i++) {
			threshold = threshold + i*nhistogram[i];
		}
		return (int) threshold;
	}

	/**
	 * 测试
	 * @param arggs
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String filePath = "G:/java/project/ImageThresholder/src/main/test2.jpg";
		Image image = ImageIO.read(new File(filePath));
		Thresholder thresholder = new GreyExpImageThresholder(image);
		thresholder.doThresholding();
		BufferedImage img = thresholder.generateBufferedImage();
		MyGUI ui = new MyGUI();
		ui.addImage("",image).addImage("", img);
		ui.setVisible(true);
	}
}
