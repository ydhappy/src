package lineage.util;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.RGBImageFilter;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import lineage.bean.util.BlockDef;
import lineage.bean.util.Frame;
import lineage.bean.util.Pak;
import lineage.network.packet.ClientBasePacket;
import lineage.share.TimeLine;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public class SpriteTools {

	private static final String SpriteIdxFileName = "resource/Sprite.idx";
	private static final String SpritePakFileName = "resource/Sprite.pak";
	private static Map<String, Pak> list;
	
	public static void init() throws Exception {
		TimeLine.start("SpriteTools..");
		
		// sprite 파일 읽기.
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(SpriteIdxFileName));
		byte[] data = new byte[bis.available()];
		bis.read(data, 0, data.length); 
		bis.close();
		//
		int num = 4;
		int num2 = (data.length - num) / 28;
		list = new HashMap<String, Pak>();
		for (int i = 0; i<num2; i++) {
			Pak p = new Pak(data, num + (i*28));
			list.put(p.FileName.toLowerCase(), p);
		}
		
		TimeLine.end();
	}
	
	public static Image getTbt(int gfx) {
		Pak p = find( String.format("%d.tbt", gfx) );
		if(p != null) {
			//
			if(p.frame.size() > 0)
				p.frame.get(p.frameIdx);
			//
			try {
				//
				byte[] data = new byte[p.FileSize];
				BufferedInputStream bos = new BufferedInputStream(new FileInputStream(SpritePakFileName));
				bos.skip(p.Offset);
				bos.read(data, 0, p.FileSize);
				bos.close();
				//
				p.frame.add( toTBT(p, data) );
				return p.frame.get(p.frameIdx);
			} catch (Exception e) { }
		}
		return null;
	}
	
	/**
	 * Sprite에서 원하는 이미지를 추출.
	 * @param gfx		: 추출할 gfx 번호
	 * @param heading	: 추출할 방향
	 * @param mode		: 추출할 모드
	 * @return
	 */
	public static Image get(int gfx, int heading, long time) {
//		군주 : 8 = 0방향 가만히 서잇음.
//		공주 : 8 = 0방향 맞는모션
//
//		군주 모션순서
//		 : 공격 -> 정지 -> 죽음 -> 걷기 -> 도끼공격 -> 도끼정지....
//		공주 모션순서
//		 : 공격 -> 맞는모션 -> 죽음 -> 걷기 -> 도끼공격 ...
//		기사 모션순서
//		 : 걷기 -> 공격 -> 맞기 -> 걷기...
		int mode = 8 * 3;
		switch(gfx) {
			case 29:
			case 49:
			case 52:
			case 54:
			case 56:
			case 57:
			case 95:
				mode = 8 * 4;
				break;
			case 31:
				heading = 0;
				mode = 18;
				break;
			case 51:
			case 94:
				mode = 8 * 5;
				break;
			case 267:
			case 335:
			case 255:
			case 257:
			case 1477:
				mode = 0;
				break;
			case 110:
			case 1022:
			case 30:
			case 145:
			case 784:
			case 786:
			case 788:
			case 2786:
			case 2796:
				mode = 8 * 1;
				break;
		}
		//
		Pak p = find( String.format("%d-%d.spr", gfx, heading + mode) );
		if(p != null) {
			//
			if(p.frame.size() > 0) {
				if(p.frameIdx >= p.frame.size())
					p.frameIdx = 0;
				int speed = 200;
				if(speed+p.frameTime <= time) {
					p.frameTime = time;
					return p.frame.get(p.frameIdx++);
				} else {
					return p.frame.get(p.frameIdx);
				}
			}
			//
			try {
				//
				byte[] data = new byte[p.FileSize];
				BufferedInputStream bos = new BufferedInputStream(new FileInputStream(SpritePakFileName));
				bos.skip(p.Offset);
				bos.read(data, 0, p.FileSize);
				bos.close();
				//
				toSprite(p, data);
				p.frameTime = time;
				return p.frame.get( p.frameIdx++ );
			} catch (Exception e) { }
		}
		return null;
	}
	
	/**
	 * 파일이름으로 찾기.
	 * @param key
	 * @return
	 */
	private static Pak find(String key) {
		return list.get(key.toLowerCase());
	}
	
	/**
	 * 스프라이트 이미지 추출 함수.
	 * @param p
	 */
	private static void toSprite(Pak p, byte[] data) throws Exception {
		//
		int pos_x=0, pos_y=0;
		int[] numArray = null;
		int mask = 0x0000;
		int num = mask;
		int[] array = new int[] { 0x7c00, 0x300, 0x1f, 0x7fe0, 0x3ff, 0x7c1f, 0x7fff };
		ClientBasePacket cbp = (ClientBasePacket)ClientBasePacket.clone(null, data, data.length);
		cbp.seek(0);
		//
		boolean flag = false;
		int num2 = cbp.readC();
		if (num2 == 0xff) {
			flag = true;
			int num3 = cbp.readC();
			if (num3 == 0)
				num3 = 0x100;
			numArray = new int[num3];
			for (int n = 0; n < num3; n++) {
				numArray[n] = cbp.readHH();
				int index = -1;
				for(int i=0 ; i<array.length ; ++i)
					if(array[i] == numArray[n])
						index = i;
				if (index >= 0)
					array[index] = 0;
			}
			for(int i=0 ; i<array.length ; ++i) {
				int num6 = array[i];
				if (num6 != 0) {
//					num = num6;
					break;
				}
			}
			num2 = cbp.readC();
		}
		//
		Frame[] frameArray = new Frame[num2];
		BlockDef[][] defArray = new BlockDef[num2][];
		for (int i = 0; i < num2; i++) {
			frameArray[i] = new Frame();
			frameArray[i].x_offset = cbp.readHH();
			frameArray[i].y_offset = cbp.readHH();
			frameArray[i].width = (cbp.readHH() - frameArray[i].x_offset) + 1;
			frameArray[i].height = (cbp.readHH() - frameArray[i].y_offset) + 1;
			frameArray[i].unknow_1 = cbp.readHH();
			frameArray[i].unknow_2 = cbp.readHH();
			int num8 = cbp.readHH();
			if (num8 > 0) {
				defArray[i] = new BlockDef[num8];
				for (int num9 = 0; num9 < num8; num9++) {
					defArray[i][num9] = new BlockDef();
					defArray[i][num9].a = cbp.readCC();
					defArray[i][num9].b = cbp.readCC();
					defArray[i][num9].FrameType = cbp.readCC();
					defArray[i][num9].BlockID = cbp.readHH();
				}
				frameArray[i].type = defArray[i][0].FrameType;
			}
			//
			if(pos_x==0 || pos_x>frameArray[i].x_offset)
				pos_x = frameArray[i].x_offset;
			if(pos_y==0 || pos_y>frameArray[i].y_offset)
				pos_y = frameArray[i].y_offset;
		}
		//
		int num10 = (int)cbp.readD();
		int[] numArray3 = new int[num10];
		for (int j = 0; j < num10; j++)
			numArray3[j] = (int)cbp.readD();
		//
		int offset = (int)cbp.readD();
		//
		int position = cbp.readIndex();
		int[][][] numArray4 = new int[num10][][];
		for (int k=0 ; k<num10 ; k++) {
			numArray4[k] = new int[0x18][0x18];
			for (int num14 = 0; num14 < 0x18; num14++)
				for (int num15 = 0; num15 < 0x18; num15++)
					numArray4[k][num14][num15] = mask;
			cbp.seek(position + numArray3[k]);
			int num16 = cbp.readC();
			int num17 = cbp.readC();
			cbp.readC();
			int num18 = cbp.readC();
			for (int num19 = 0; num19 < num18; num19++) {
				int num20 = num16;
				int num21 = cbp.readC();
				for (int num22 = 0; num22 < num21; num22++) {
					num20 += cbp.readC() / 2;
					int num23 = cbp.readC();
					for (int num24 = 0; num24 < num23; num24++) {
						if (flag) {
							numArray4[k][num19 + num17][num20] = numArray[cbp.readC()];
						} else {
							int num25 = cbp.readHH();
							numArray4[k][num19 + num17][num20] = num25;
							int num26 = -1;
							for(int i=0 ; i<array.length ; ++i)
								if(array[i] == num25)
									num26 = i;
							if (num26 >= 0)
								array[num26] = 0;
						}
						num20++;
					}
				}
			}
		}
		// 배경색상 원본으로 변경하는거임..필요 없더!! 내가 원하는걸로 할고얏!!
        if (num == 0x8000) {
        	for(int i=0 ; i<array.length ; ++i) {
        		int num27 = array[i];
                if (num27 != 0) {
                    num = num27;
                    break;
                }
            }
        }
		//
        for (int m = 0; m < num2; m++) {
        	if (defArray[m] != null) {
                Frame frame = frameArray[m];
                frameArray[m].maskcolor = num;
                //
                byte[] bmpdata = new byte[(frame.height * (frame.width + (frame.width % 2))) * 2];
                for (int num29 = 0; num29 < bmpdata.length; num29++) {
                	bmpdata[num29] = (byte) (num & 0xff);
                	bmpdata[++num29] = (byte) ((num & 0xff00) >> 8);
                }
                for (int num30 = 0 ; num30 < defArray[m].length ; num30++) {
                    int a = defArray[m][num30].a;
                    if (a < 0)
                        a--;
                    int num32 = 0x18 * ((defArray[m][num30].b + defArray[m][num30].a) - (a / 2));
                    int num33 = 12 * (defArray[m][num30].b - (a / 2));
                    int[][] numArray5 = numArray4[defArray[m][num30].BlockID];
                    for (int num34 = 0; num34 < 0x18; num34++) {
                        for (int num35 = 0; num35 < 0x18; num35++) {
                            int num36 = numArray5[num34][num35];
                            if (num36 != mask) {
                            	// 24 -48
                                int num37 = num32 + num35;
                                int num38 = num33 + num34;
                                if (((num37 >= frame.x_offset) && (num37 < (frame.width + frame.x_offset))) && ((num38 >= frame.y_offset) && (num38 < (frame.height + frame.y_offset)))) {
                                    int num39 = (((num38 - frame.y_offset) * (frame.width + (frame.width % 2))) + (num37 - frame.x_offset)) * 2;
                                    bmpdata[num39] = (byte) (num36 & 0xff);
                                    bmpdata[++num39] = (byte) ((num36 & 0xff00) >> 8);
                                }
                            }
                        }
                    }
                }
        		//
                Image buffImg = CreateBMP(frameArray[m].width, frameArray[m].height, bmpdata, 0, frameArray[m].maskcolor, frameArray[m].x_offset, frameArray[m].y_offset, pos_x, pos_y);
                p.frame.add( buffImg );
        	}
        }
	}
	
	private static Image CreateBMP(int width, int height, byte[] src, int index, int maskColor, int x_offset, int y_offset, int pos_x, int pos_y) throws Exception {
		// ----------- 중복 코드.
		int Format16bppRgb555 = 135173;								// 이미지의 각 픽셀에 대한 색 데이터의 형식을 지정합니다.
		int bitsPerPixel = (Format16bppRgb555 & 0xff00) >> 8;
        int bytesPerPixel = (bitsPerPixel + 7) / 8;
        int stride = 4 * ((width * bytesPerPixel + 3) / 4);
		int num = stride / 1;
		byte[] destinationArray = new byte[num * height];
		//
		if ((src.length - index) == destinationArray.length) {
			System.arraycopy(src, index, destinationArray, 0, destinationArray.length);
		} else {
			for (int i = 0; i < height; i++)
				System.arraycopy(src, index + ((i * width) * 2), destinationArray, i * num, width * 2);
		}
		// --------- 중복 코드.
        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_555_RGB);
        WritableRaster raster = buffImg.getRaster();
        for(int y=0 ; y<height ; y++) {
        	int x = 0;
        	for(int i=0 ; i<stride ; i+=2,x++) {
        		// - rgb555
        		// XRRRRRGG GGGBBBBB 이렇게 16bit를 구성 (X는 더미비트)
        		// - rgb565
            	//RRRRRGGG GGGBBBBB
        		int idx = (y*stride) + i;
        		int pixel = destinationArray[idx] &0xff;
        		pixel |= destinationArray[idx+1] << 8 &0xff00;
        		
        		//
        		if(pixel>0 && x<width) {
        			// rgb555
        			int r = (pixel>>10) &0x1F;
        			int g = (pixel>>5) &0x1F;
        			int b = pixel &0x1F;
	        		//
		    		raster.setPixel(x, y, new int[]{r, g, b});
        		}
        	}
        }
        
        // 리 사이즈 필요 없음.
//    	int targetSize = 100;
//    	if(width>targetSize*2 && height>targetSize*2) {
//    		if(width>targetSize)
//    			width >>= 1;
//			if(height > targetSize)
//				height >>= 1;
//    	} else {
//    		double resizeRatio = 1.0;
//    		if(width > height)
//    			resizeRatio = (double)targetSize / width;
//    		else
//    			resizeRatio = (double)targetSize / height;
//    		width = (int)(width * resizeRatio);
//    		height = (int)(height * resizeRatio);
//    	}
//    	
//    	BufferedImage tempImage = new BufferedImage(targetSize, targetSize, BufferedImage.TYPE_INT_ARGB);
//    	Graphics2D g2 = tempImage.createGraphics();
//    	g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//   		g2.drawImage(buffImg, x_offset-pos_x, y_offset-pos_y, width, height, null);
//    	g2.dispose();
//    	
//    	buffImg = tempImage;
    	
    	//
        return TransformColorToTransparency(buffImg, new Color(0, 0, 0), new Color(10, 10, 10));
	}
	
	public static Image toTBT(Pak p, byte[] data) {
		//
		ChannelBuffer cb = ChannelBuffers.copiedBuffer(data);
		int x = cb.readByte();
		int y = cb.readByte();
        int width = cb.readByte();
        int height = cb.readByte();
        if ((width == 0) || (height == 0))
        	return null;
		//
		int Format16bppRgb555 = 135173;								// 이미지의 각 픽셀에 대한 색 데이터의 형식을 지정합니다.
		int bitsPerPixel = (Format16bppRgb555 & 0xff00) >> 8;
        int bytesPerPixel = (bitsPerPixel + 7) / 8;
        int stride = 4 * ((width * bytesPerPixel + 3) / 4);
		//
        byte[] destinationArray = new byte[height * stride];
        for(int i=0 ; i<height ; i++) {
        	int num5 = cb.readByte();
        	int num6 = 0;
            for(int j=0 ; j<num5 ; j++) {
            	num6 += cb.readByte();
            	int length = cb.readByte() * 2;
       			System.arraycopy(cb.readBytes(length).array(), 0, destinationArray, (i*stride) + num6, length);
            	num6 += length;
            }
        }
//        cb.clear();
        //
        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_555_RGB);
        WritableRaster raster = buffImg.getRaster();
        for(y=0 ; y<height ; y++) {
        	x = 0;
        	for(int i=0 ; i<stride ; i+=2,x++) {
        		// - rgb555
        		// XRRRRRGG GGGBBBBB 이렇게 16bit를 구성 (X는 더미비트)
        		// - rgb565
            	//RRRRRGGG GGGBBBBB
        		int idx = (y*stride) + i;
        		int pixel = destinationArray[idx] &0xff;
        		pixel |= destinationArray[idx+1] << 8 &0xff00;
        		
        		//
        		if(pixel > 0) {
        			// rgb555
        			int r = (pixel>>10) &0x1F;
        			int g = (pixel>>5) &0x1F;
        			int b = pixel &0x1F;
	        		//
		    		raster.setPixel(x, y, new int[]{r, g, b});
        		}
        	}
        }
        return TransformColorToTransparency(buffImg, new Color(0, 0, 0), new Color(10, 10, 10));
	}
	
	private static Image TransformColorToTransparency(BufferedImage image, Color c1, Color c2) {
		// Primitive test, just an example
		final int r1 = c1.getRed();
		final int g1 = c1.getGreen();
		final int b1 = c1.getBlue();
		final int r2 = c2.getRed();
		final int g2 = c2.getGreen();
		final int b2 = c2.getBlue();
		ImageFilter filter = new RGBImageFilter() {
			@Override
			public final int filterRGB(int x, int y, int rgb) {
				int r = (rgb & 0xFF0000) >> 16;
				int g = (rgb & 0xFF00) >> 8;
				int b = rgb & 0xFF;
				if (r >= r1 && r <= r2 &&
					g >= g1 && g <= g2 &&
					b >= b1 && b <= b2) {
					// Set fully transparent but keep color
					return rgb & 0xFFFFFF;
				}
				return rgb;
			}
		};
		//
		return Toolkit.getDefaultToolkit().createImage( new FilteredImageSource(image.getSource(), filter) );
	}

}
