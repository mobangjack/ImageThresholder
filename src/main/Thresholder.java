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

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author 帮杰
 *
 */
public abstract class Thresholder {

	//image width
	protected int w;
	//image height
	protected int h;
	//grey pixels array
	protected int[] pixels;
	//threshold
	private int threshold;
	//grey color model
	protected static final ColorModel COLOR_MODEL = new ColorModel(8) {
		
		@Override
		public int getRed(int pixel) {
			return pixel;
		}
		
		@Override
		public int getGreen(int pixel) {
			return pixel;
		}
		
		@Override
		public int getBlue(int pixel) {
			return pixel;
		}
		
		@Override
		public int getAlpha(int pixel) {
			return pixel;
		}
	};
	
	public Thresholder(Image image) {
		w = image.getWidth(null);
		h = image.getHeight(null);
		pixels = new int[w*h];
		PixelGrabber pg = new PixelGrabber(image, 0, 0, w, h, pixels, 0, w);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		greyScale(0.3f, 0.59f, 0.11f);
	}
	
	public Thresholder(File imageFile) throws IOException {
		this(ImageIO.read(imageFile));
	}
	
	public Thresholder(String imageFilePath) throws IOException {
		this(new File(imageFilePath));
	}
	
	/**
	 * Change the RGB color model to grey scale by their weighting.
	 * The classical weighting vector is (0.3,0.59,0.11).
	 * <p>用加权法将RGB图像灰度化，典型的权值向量为(0.3,0.59,0.11)
	 * @param wr
	 * @param wg
	 * @param wb
	 */
	protected void greyScale(float wr,float wg,float wb) {
		float s = wr+wg+wb;
		wr/=s;
		wg/=s;
		wb/=s;
		for (int i = 0; i < pixels.length; i++) {
			int pixel = pixels[i];
			ColorModel cm = ColorModel.getRGBdefault();
			int r = cm.getRed(pixel);
			int g = cm.getGreen(pixel);
			int b = cm.getBlue(pixel);
			int grey = (int) (r*wr+g*wg+b*wb);
			pixels[i] = grey;
		}
	}
	
	/**
	 * calculate histogram
	 * <p>计算归灰度能量分布
	 * @return
	 */
	public int[] histogram() {
		int[] histogram = new int[256];
		for (int i = 0; i < pixels.length; i++) {
			histogram[pixels[i]]++;
		}
		return histogram;
	}
	
	/**
	 * calculate normalized histogram
	 * <p>计算归一化灰度能量分布
	 * @return
	 */
	public double[] nhistogram() {
		double[] nhistogram = new double[256];
		for (int i = 0; i < pixels.length; i++) {
			nhistogram[pixels[i]]++;
		}
		for (int i = 0; i < nhistogram.length; i++) {
			nhistogram[i] = nhistogram[i]/(pixels.length);
		}
		return nhistogram;
	}
	
	/**
	 * calculate cumulative distribution
	 * <p>计算累积分布
	 * @param histogram
	 * @return
	 */
	public double[] cumulative(int[] histogram) {
		double[] cumulative = new double[256];
		cumulative[0] = histogram[0];
		for (int i = 1; i < histogram.length; i++) {
			cumulative[i] = cumulative[i-1] + histogram[i];
		}
		return cumulative;
	}
	
	/**
	 * calculate cumulative distribution
	 * <p>计算累积分布
	 * @param nhistogram
	 * @return
	 */
	public double[] cumulative(double[] nhistogram) {
		double[] cumulative = new double[256];
		cumulative[0] = nhistogram[0];
		for (int i = 1; i < nhistogram.length; i++) {
			cumulative[i] = cumulative[i-1] + nhistogram[i];
		}
		return cumulative;
	}
	
	/**
	 * calculate threshold
	 * <p>计算阈值
	 * @return
	 */
	public abstract int threshold();
	
	/**
	 * 二值分割
	 * @param th
	 */
	public void doThresholding(int threshold) {
		for (int i = 0; i < pixels.length; i++) {
			if (pixels[i]>threshold) {
				pixels[i] = 255;
			}else {
				pixels[i] = 0;
			}
		}
	}
	
	/**
	 * 二值分割
	 */
	public void doThresholding() {
		threshold = threshold();
		System.out.println(getClass().getSimpleName()+"-Threshold:"+threshold);
		doThresholding(threshold);
	}
	
	public int getThreshold() {
		return threshold;
	}

	/**
	 * generate BufferedImage
	 * <p>生成图像
	 * @return
	 */
	public BufferedImage generateBufferedImage() {
		BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
		Graphics2D g = bufferedImage.createGraphics();
		Image img = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(w, h, COLOR_MODEL, pixels, 0, w));
		g.drawImage(img, 0, 0, null);
		return bufferedImage;
	}
}
