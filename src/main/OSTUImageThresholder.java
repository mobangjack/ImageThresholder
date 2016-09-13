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
 * 最大类间方差法（OSTU） 
 * 最大类间方差法也称大律法，其基本原理是：对一幅图像，
 * 记T为目标与背景的分割阈值，T的取值范围是从最小灰度
 * 值到最大灰度值；目标点数占图像像素点数的比例为w1，
 * 平均灰度为a；背景点数占图像像素点数的比例为w2, 平
 * 均灰度为b。图像的总平均灰度为u。则两类间方差 的计算
 * 公式为：σ^2=w1*(a-u)^2+w2*(b-u)^2 。即阈值T将图像
 * 分成目标和背景两部分，使得两类总方差 取最大值的T即
 * 为最佳分割阈值。因为方差是图像灰度分布均匀性的一种
 * 度量，方差越大说明构成图像的两部分差 别越大，而部分
 * 目标错分为背景或部分背景错分为目标都会导致两部分差
 * 别变小，因此使类 间方差最大的分割意味着错分概率最小
 * 大量实验结果表明OSTU算法对图像的分割质量 通常都有一
 * 定的保障，对各种情况的表现都较为良好，可以说是比较
 * 好的一种分割算法。 
 * @author 帮杰
 *
 */
public class OSTUImageThresholder extends Thresholder {

	/**
	 * @param image
	 */
	public OSTUImageThresholder(Image image) {
		super(image);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param imageFile
	 * @throws IOException
	 */
	public OSTUImageThresholder(File imageFile) throws IOException {
		super(imageFile);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param imageFilePath
	 * @throws IOException
	 */
	public OSTUImageThresholder(String imageFilePath) throws IOException {
		super(imageFilePath);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see main.Thresholder#threshold()
	 */
	@Override
	public int threshold() {
		int threshold = 0;
		double maxVariance = 0;
		for (int i = 0; i < 256; i++) {
			double targetGrey = 0;
			double bgGrey = 0;
			double target = 0;
			double bg = 0;
			for (int j = 0; j < pixels.length; j++) {
				if (pixels[j]<i) {
					targetGrey = targetGrey + pixels[j];
					target++;
				}else {
					bgGrey = bgGrey +pixels[j];
					bg++;
				}
			}
			double grey = (targetGrey + bgGrey)/pixels.length;
			targetGrey = targetGrey/target;
			bgGrey = bgGrey/bg;
			target = target/pixels.length;
			bg = bg/pixels.length;
			double variance = target*(Math.pow(targetGrey-grey, 2)+bg*(Math.pow(bgGrey-grey, 2)));
			if (variance>maxVariance) {
				maxVariance = variance;
				threshold = i;
			}
		}
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
		Thresholder thresholder = new OSTUImageThresholder(image);
		thresholder.doThresholding();
		BufferedImage img = thresholder.generateBufferedImage();
		MyGUI ui = new MyGUI();
		ui.addImage("",image).addImage("", img);
		ui.setVisible(true);
	}
}
