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
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * 直方图谷点阈值选取算法 
 * <br>这是最简单的一种图像阈值分割算法，
 * 一般是根据图像的直方图来进行的。
 * <br>基本原理是：
 * <br>如果图像的目标和背景区域的灰度差异较大，则该图像的灰度直方图包络线就呈现双峰一谷
 * 的曲线，那么选取两峰之间的谷值就可以作为阈值来分割图像的目标和背景。这种方法在
 * 图像的目标和背景之间的灰度差异较为明显时，可以取得良好的分割效果，通常可以满足我们的分割要求。
 * 虽然由于该方法对图像直方图的特殊要求和依赖，使其在图像分割中具有一
 * 定的局限性，但其操作简单运算量低，因此也被经常使用。
 * @author 帮杰
 *
 */
public class HistogramValleyPointImageThresholder extends Thresholder {

	/**
	 * @param image
	 */
	public HistogramValleyPointImageThresholder(Image image) {
		super(image);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param imageFile
	 * @throws IOException
	 */
	public HistogramValleyPointImageThresholder(File imageFile) throws IOException {
		super(imageFile);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param imageFilePath
	 * @throws IOException
	 */
	public HistogramValleyPointImageThresholder(String imageFilePath)
			throws IOException {
		super(imageFilePath);
		// TODO Auto-generated constructor stub
	}

	public List<Integer> valleyList(int delta) {
		List<Integer> valleyList = new ArrayList<Integer>();
		int[] histogram = histogram();
		for (int i = delta; i < histogram.length-delta; i++) {
			boolean isValley = true;
			for (int j = i-delta; j < i+delta; j++) {
				if (histogram[j]<histogram[i]) {
					isValley = false;
				}
			}
			if (isValley) {
				valleyList.add(i);
			}
		}
		return valleyList;
	}
	
	public List<Integer> valleyList() {
		return valleyList(10);
	}
	
	/* (non-Javadoc)
	 * @see main.Thresholder#threshold()
	 */
	@Override
	public int threshold() {
		int threshold = 0;
		int[] histogram = histogram();
		int delta = 30;
		for (int i = delta; i < histogram.length-delta; i++) {
			boolean isValley = true;
			for (int j = i-delta; j < i+delta; j++) {
				if (histogram[j]>histogram[i]) {
					isValley = false;
				}
			}
			if (isValley) {
				threshold = i;
			}
		}
		return threshold;
	}

	/**
	 * 测试
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String filePath = "G:/java/project/ImageThresholder/src/main/test2.jpg";
		Image image = ImageIO.read(new File(filePath));
		Thresholder thresholder = new HistogramValleyPointImageThresholder(image);
		thresholder.doThresholding();
		BufferedImage img = thresholder.generateBufferedImage();
		MyGUI ui = new MyGUI();
		ui.addImage("",image).addImage("", img);
		ui.setVisible(true);
	}
}
