package com.felipebicca.cursomc.services;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.felipebicca.cursomc.services.exception.FileException;

@Service
public class ImageService {
	public BufferedImage getJpgImageFromFile(MultipartFile uploadedFile) {
		String ext = FilenameUtils.getExtension(uploadedFile.getOriginalFilename()).toLowerCase();

		if (!"png".equals(ext) && !"jpg".equals(ext)) {
			throw new FileException("Somente imagens PNG e JPG são permitidos");
		}

		try {
			BufferedImage img = ImageIO.read(uploadedFile.getInputStream());
			if ("png".equals(ext)) {
				img = pngToJpg(img);
			}
			return img;
		} catch (IOException e) {
			throw new FileException("Erro ao ler arquivo");
		}
	}

	public BufferedImage pngToJpg(BufferedImage img) {
		BufferedImage jpgImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		jpgImage.createGraphics().drawImage(img, 0, 0, Color.WHITE, null);
		return jpgImage;
	}

	public InputStream getInputStream(BufferedImage img, String extension) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(img, extension, os);
			return new ByteArrayInputStream(os.toByteArray());
		} catch (IOException e) {
			throw new FileException("Erro ao ler arquivo");
		}

	}

	public BufferedImage cropSquare(BufferedImage sourceImg) {
		int min = (sourceImg.getHeight() <= sourceImg.getWidth()) ? sourceImg.getHeight() : sourceImg.getWidth();
		return Scalr.crop(sourceImg, (sourceImg.getWidth() / 2) - (min / 2), (sourceImg.getHeight() / 2) - (min / 2),
				min, min);
	}

	public BufferedImage resize(BufferedImage sourceImg, int size) {
		return Scalr.resize(sourceImg, Scalr.Method.ULTRA_QUALITY, size);
	}

	public static BufferedImage RotateImage(BufferedImage imageToRotate, int radiusRotate) {
		int widthOfImage = imageToRotate.getWidth();
		int heightOfImage = imageToRotate.getHeight();
		int typeOfImage = imageToRotate.getType();

		BufferedImage newImageFromBuffer = new BufferedImage(widthOfImage, heightOfImage, typeOfImage);

		Graphics2D graphics2D = newImageFromBuffer.createGraphics();

		graphics2D.rotate(Math.toRadians(radiusRotate), widthOfImage / 2, heightOfImage / 2);
		graphics2D.drawImage(imageToRotate, null, 0, 0);

		return newImageFromBuffer;
	}
}
