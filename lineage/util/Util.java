package lineage.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.Character.UnicodeBlock;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import lineage.bean.lineage.Map;
import lineage.bean.lineage.Swap;
import lineage.share.Lineage;
import lineage.world.World;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.monster.TrapArrow;
import lineage.world.object.npc.kingdom.KingdomGuard;

public final class Util {

	static private Date date = new Date(0);
	static private DateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
	static private DateFormat date_format_detail = new SimpleDateFormat("yyyy-MM-dd HH시mm분ss초");
	static private DateFormat date_format_time = new SimpleDateFormat("HH:mm:ss");
	static private String[] weekDay = { "일", "월", "화", "수", "목", "금", "토" };// 여기네용

	/**
	 * 시간에 해당하는 요일을 리턴.
	 * 
	 * @param time
	 * @return
	 */
	static public String getYoil(long time) {
		date.setTime(time);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return weekDay[c.get(Calendar.DAY_OF_WEEK) - 1];
	}

	/**
	 * 시간에 해당하는 년 값 추출.
	 * 
	 * @param time
	 * @return
	 */
	@SuppressWarnings("deprecation")
	static public int getYear(long time) {
		date.setTime(time);
		return date.getYear();
	}

	/**
	 * 시간에 해당하는 월 값 추출.
	 * 
	 * @param time
	 * @return
	 */
	@SuppressWarnings("deprecation")
	static public int getMonth(long time) {
		date.setTime(time);
		return date.getMonth() + 1;
	}

	/**
	 * 시간에 해당하는 일 값 추출.
	 * 
	 * @param time
	 * @return
	 */
	@SuppressWarnings("deprecation")
	static public int getDate(long time) {
		date.setTime(time);
		return date.getDate();
	}

	/**
	 * 시간에 해당하는 시 값 추출.
	 * 
	 * @param time
	 * @return
	 */
	@SuppressWarnings("deprecation")
	static public int getHours(long time) {
		date.setTime(time);
		return date.getHours();
	}

	/**
	 * 시간에 해당하는 분 값 추출.
	 * 
	 * @param time
	 * @return
	 */
	@SuppressWarnings("deprecation")
	static public int getMinutes(long time) {
		date.setTime(time);
		return date.getMinutes();
	}

	/**
	 * 시간에 해당하는 초 값 추출.
	 * 
	 * @param time
	 * @return
	 */
	@SuppressWarnings("deprecation")
	static public int getSeconds(long time) {
		date.setTime(time);
		return date.getSeconds();
	}

	static public String getLocaleString(long time, boolean detail) {
		date.setTime(time);
		return detail ? date_format_detail.format(date) : date_format.format(date);
	}

	/**
	 * 밀리세컨드값을 문자열로 변환해줌
	 * 00:00:00
	 * 
	 * @param time
	 * @return
	 */
	static public String getLocaleString(long time) {
		date.setTime(time);
		return date_format_time.format(date);
	}

	/**
	 * yyyy-mm-dd 값으로 밀리세컨드값 추출.
	 * 
	 * @param date
	 * @return
	 */
	static public Long getTime(String date) {
		int year = Integer.valueOf(date.substring(0, 4)) - 1900;
		int month = Integer.valueOf(date.substring(5, 7)) - 1;
		int day = Integer.valueOf(date.substring(8, 10));
		int h = date.length() > 10 ? Integer.valueOf(date.substring(11, 13)) : 0;
		int m = date.length() > 10 ? Integer.valueOf(date.substring(14, 16)) : 0;
		int s = date.length() > 10 ? Integer.valueOf(date.substring(17, 19)) : 0;
		Date time = new Date(0);
		time.setYear(year);
		time.setMonth(month);
		time.setDate(day);
		time.setHours(h);
		time.setMinutes(m);
		time.setSeconds(s);
		return time.getTime();
	}

	/**
	 * 랜덤값 추출용 함수.
	 * 
	 * @param lbound
	 * @param ubound
	 * @return
	 */
	static public int random(int lbound, int ubound) {
		if (ubound < 0)
			return (int) ((Math.random() * (ubound - lbound - 1)) + lbound);
		else
			return (int) ((Math.random() * (ubound - lbound + 1)) + lbound);
	}

	static public long random(long lbound, long ubound) {
		return (long) ((Math.random() * (ubound - lbound + 1)) + lbound);
	}

	static public double random(double lbound, double ubound) {
		return (Math.random() * (ubound - lbound + 1)) + lbound;
	}

	/**
	 * 거리안에 있다면 참
	 */
	static public boolean isDistance(int x, int y, int m, int tx, int ty,
			int tm, int loc) {
		int distance = getDistance(x, y, tx, ty);
		if (loc < distance)
			return false;
		if (m != tm)
			return false;
		return true;
	}

	/**
	 * 거리안에 있다면 참
	 */
	static public boolean isDistance(object o, object oo, int loc) {
		if (o == null || oo == null)
			return false;

		return isDistance(o.getX(), o.getY(), o.getMap(), oo.getX(), oo.getY(),
				oo.getMap(), loc);
	}

	/**
	 * 거리값 추출.
	 * 
	 * @param o
	 * @param oo
	 * @return
	 */
	static public int getDistance(object o, object oo) {
		return getDistance(o.getX(), o.getY(), oo.getX(), oo.getY());
	}

	/**
	 * 거리값 추출.
	 * 
	 * @param o
	 * @param oo
	 * @return
	 */
	static public int getDistance(int x, int y, int tx, int ty) {
		long dx = tx - x;
		long dy = ty - y;
		return (int) Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * 해당하는 좌표로 방향을 전환할때 사용.
	 */
	static public int calcheading(int myx, int myy, int tx, int ty) {
		if (tx > myx && ty > myy) {
			return 3;
		} else if (tx < myx && ty < myy) {
			return 7;
		} else if (tx > myx && ty == myy) {
			return 2;
		} else if (tx < myx && ty == myy) {
			return 6;
		} else if (tx == myx && ty < myy) {
			return 0;
		} else if (tx == myx && ty > myy) {
			return 4;
		} else if (tx < myx && ty > myy) {
			return 5;
		} else {
			return 1;
		}
	}

	static public int calcheading(object o, int x, int y) {
		return calcheading(o.getX(), o.getY(), x, y);
	}

	static public int calcheading(object o, object t) {
		return calcheading(o.getX(), o.getY(), t.getX(), t.getY());
	}

	/**
	 * 객체를 참고로 반대 방향 리턴.
	 */
	static public int oppositionHeading(object o, object oo) {
		int myx = o.getX();
		int myy = o.getY();
		int tx = oo.getX();
		int ty = oo.getY();
		if (tx > myx && ty > myy) {
			return 7;
		} else if (tx < myx && ty < myy) {
			return 3;
		} else if (tx > myx && ty == myy) {
			return 6;
		} else if (tx < myx && ty == myy) {
			return 2;
		} else if (tx == myx && ty < myy) {
			return 4;
		} else if (tx == myx && ty > myy) {
			return 0;
		} else if (tx < myx && ty > myy) {
			return 1;
		} else {
			return 5;
		}
	}

	/**
	 * 방향과 타입에따라 적절하게 좌표값세팅 리턴
	 * 
	 * @param h
	 *             : 방향
	 * @param type
	 *             : true ? x : y
	 * @return
	 */
	static public int getXY(final int h, final boolean type) {
		int loc = 0;
		switch (h) {
			case 0:
				if (!type)
					loc -= 1;
				break;
			case 1:
				if (type)
					loc += 1;
				else
					loc -= 1;
				break;
			case 2:
				if (type)
					loc += 1;
				break;
			case 3:
				loc += 1;
				break;
			case 4:
				if (!type)
					loc += 1;
				break;
			case 5:
				if (type)
					loc -= 1;
				else
					loc += 1;
				break;
			case 6:
				if (type)
					loc -= 1;
				break;
			case 7:
				loc -= 1;
				break;
		}
		return loc;
	}

	/**
	 * 화폐 자릿수 포맷 변환 메소드.
	 * 2018-07-20
	 * by connector12@nate.com
	 */
	static public String changePrice(long price) {
		return String.format("%,d", price);
	}

	/**
	 * 원하는 타켓에게 장거리 공격 및 근거리 공격이 가능한지 체크
	 */
	static public boolean isAreaAttack(object o, object target) {
		// 성에 관련된 객체 장애물 무시하기.
		if (o instanceof KingdomGuard || target instanceof KingdomGuard)
			return true;
		// 가격자가 및 타격자가 TrapArrow라면 장애물 무시하기.
		if (o instanceof TrapArrow || target instanceof TrapArrow)
			return true;
		//
		int myx = o.getX();
		int myy = o.getY();
		int map = o.getMap();
		int tax = target.getX();
		int tay = target.getY();
		int count = Lineage.SEARCH_LOCATIONRANGE;
		int h = 0;
		boolean o_is = true;
		boolean target_is = true;
		//
		myx = o.getX();
		myy = o.getY();
		count = Lineage.SEARCH_LOCATIONRANGE;
		while (((myx != tax) || (myy != tay)) && (--count > 0)) {
			h = calcheading(myx, myy, tax, tay);
			if (!World.isThroughAttack(myx, myy, map, h)) {
				o_is = false;
				break;
			}
			switch (h) {
				case 0:
					--myy;
					break;
				case 1:
					++myx;
					--myy;
					break;
				case 2:
					++myx;
					break;
				case 3:
					++myx;
					++myy;
					break;
				case 4:
					++myy;
					break;
				case 5:
					--myx;
					++myy;
					break;
				case 6:
					--myx;
					break;
				default:
					--myx;
					--myy;
					break;
			}
		}
		//
		myx = target.getX();
		myy = target.getY();
		tax = o.getX();
		tay = o.getY();
		count = Lineage.SEARCH_LOCATIONRANGE;
		while (((myx != tax) || (myy != tay)) && (--count > 0)) {
			h = calcheading(myx, myy, tax, tay);
			if (!World.isThroughAttack(myx, myy, map, h)) {
				target_is = false;
				break;
			}
			switch (h) {
				case 0:
					--myy;
					break;
				case 1:
					++myx;
					--myy;
					break;
				case 2:
					++myx;
					break;
				case 3:
					++myx;
					++myy;
					break;
				case 4:
					++myy;
					break;
				case 5:
					--myx;
					++myy;
					break;
				case 6:
					--myx;
					break;
				default:
					--myx;
					--myy;
					break;
			}
		}

		return o_is && target_is;
	}

	/**
	 * 귀환 좌표값을 랜덤으로 생성해서 정의하는 함수.
	 * 
	 * @param o
	 */
	static public void toRndLocation(object o) {
		Map m = World.get_map(o.getMap());
		if (m != null) {
			int max = 100;
			int x1 = m.locX1;
			int x2 = m.locX2;
			int y1 = m.locY1;
			int y2 = m.locY2;
			if (m.mapid == 4) {
				// 4번맵은 12시방향에 공백이 있는데 그곳을 이동가능한 지역으로 판단하는 문제가 있어서 임의로 범위를 좁힘.
				// 공식이 이을텐데 못찾아서 임시로 이렇게 처리..
				x1 = 32520;
				y1 = 32200;
			}
			if (m.mapid == 111) {
				// 공백 잇어서 그부분 접근 안되도록 하기 위해.
				y1 = 32760;
			}
			if (m.mapid == 304) {
				// 공백 잇어서 그부분 접근 안되도록 하기 위해.
				y1 = 32770;
			}

			int count = 100;
			do {
				if (count-- < 0) {
					break;
				}
				o.setHomeX(random(x1, x2));
				o.setHomeY(random(y1, y2));
				if (--max < 0) {
					o.setHomeX(o.getX());
					o.setHomeY(o.getY());
					break;
				}
			} while (!World.isThroughObject(o.getHomeX(), o.getHomeY() + 1,
					m.mapid, 0));
		} else {
			o.setHomeX(o.getX());
			o.setHomeY(o.getY());
		}
		o.setHomeMap(o.getMap());
	}

	/**
	 * 네임아이디의 $를 제거해서 정수를 리턴하는 함수.
	 */
	static public int NameidToNumber(final String nameid) {
		StringTokenizer st = new StringTokenizer(nameid, " $ ");
		StringBuffer sb = new StringBuffer();
		while (st.hasMoreTokens()) {
			sb.append(st.nextToken());
		}

		return Integer.valueOf(sb.toString().trim());
	}

	/**
	 * 패킷 출력 함수
	 * 
	 * @param data
	 * @param len
	 * @return
	 */
	static public String printData(byte[] data, int len) {
		StringBuffer result = new StringBuffer();
		int counter = 0;
		for (int i = 0; i < len; i++) {
			if (counter % 16 == 0)
				result.append(String.format("%04x: ", i));
			result.append(String.format("%02x ", data[i] & 0xff));
			counter++;
			if (counter == 16) {
				result.append("   ");
				int charpoint = i - 15;
				for (int a = 0; a < 16; a++) {
					int t1 = data[charpoint++];
					if (t1 > 0x1f && t1 < 0x80) {
						result.append((char) t1);
					} else {
						result.append('.');
					}
				}
				result.append("\n");
				counter = 0;
			}
		}

		int rest = len % 16;
		if (rest > 0) {
			for (int i = 0; i < 17 - rest; i++) {
				result.append("   ");
			}

			int charpoint = len - rest;
			for (int a = 0; a < rest; a++) {
				int t1 = data[charpoint++];
				if (t1 > 0x1f && t1 < 0x80) {
					result.append((char) t1);
				} else {
					result.append('.');
				}
			}

			result.append("\n");
		}
		return result.toString();
	}

	static public byte[] StringToByte(String line) {
		if (line == null || line.length() == 0 || (line.length() % 2) != 0)
			return new byte[0];
		byte[] b = new byte[line.length() / 2];
		for (int i = 0, j = 0; i < line.length(); i += 2, j++) {
			b[j] = (byte) Integer.parseInt(line.substring(i, i + 2), 16);
		}
		return b;
	}

	/**
	 * 시스템이 이용중의 heap 사이즈를 메가바이트 단위로 돌려준다.<br>
	 * 이 값에 스택의 사이즈는 포함되지 않는다.
	 * 
	 * @return 이용중의 heap 사이즈
	 */
	static public long getMemoryMB() {
		return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime()
				.freeMemory()) / 1024L / 1024L;
	}

	/**
	 * 풀링에 추가해도되는지 확인해주는 함수. : 너무 많이 등록되면 문제가 되기대문에 적정선으로 카바.. :
	 * java.lang.OutOfMemoryError: Java heap space
	 * 
	 * @return
	 */
	static public boolean isPoolAppend(List<?> pool) {
		// 전체 갯수로 체크.
		return Lineage.pool_max == 0 || pool.size() < Lineage.pool_max;
	}

	/**
	 * md5 해쉬코드 얻기.
	 * 
	 * @param str
	 * @return
	 */
	public static String toMD5(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			StringBuffer sb = new StringBuffer();
			for (byte data : md.digest())
				sb.append(Integer.toString((data & 0xff) + 0x100, 16).substring(1));
			return sb.toString();

		} catch (Exception e) {
		}
		return null;
	}

	public static boolean isRange(int a, int b, int range) {
		int c = a - b;
		if (c < 0)
			c = ~c + 1;
		return c <= range;
	}

	public static boolean runIExplore(String parameter) {
		//
		Runtime runtime = Runtime.getRuntime();

		try {
			//
			StringBuffer sb = new StringBuffer();
			Process prc = runtime.exec("reg query HKEY_CLASSES_ROOT\\Applications\\iexplore.exe\\shell\\open\\command");
			int exitValue = prc.waitFor();
			if (exitValue == 0) {
				BufferedReader br = new BufferedReader(new InputStreamReader(prc.getInputStream()));
				//
				String temp = null;
				while ((temp = br.readLine()) != null)
					sb.append(temp);
				//
				int pos_a = sb.indexOf("\"") + 1;
				int pos_b = sb.indexOf("\"", pos_a);
				String path = sb.substring(pos_a, pos_b);
				//
				String[] command = new String[2];
				command[0] = path;
				command[1] = parameter;

				Runtime.getRuntime().exec(command);
				return true;
			}
		} catch (Exception e) {
		}

		//
		return false;
	}

	public static String getMapName(Character cha) {
		String local = null;

		switch (cha.getMap()) {
			case 0:
				local = "[말하는섬]";
				break;
			case 1:
				local = "[말하는섬 던전 1층]";
				break;
			case 2:
				local = "[말하는섬 던전 2층]";
				break;
			case 3:
				local = "[군터의 집]";
				break;
			case 4:
				if (cha.getX() >= 33315 && cha.getX() <= 33354 && cha.getY() >= 32430 && cha.getY() <= 32463) {
					local = "[용의 계곡 삼거리]";
					break;
				} else if (cha.getX() >= 33248 && cha.getX() <= 33284 && cha.getY() >= 32374 && cha.getY() <= 32413) {
					local = "[용의 계곡 작은뼈]";
					break;
				} else if (cha.getX() >= 33374 && cha.getX() <= 33406 && cha.getY() >= 32319 && cha.getY() <= 32357) {
					local = "[용의 계곡 큰뼈]";
					break;
				} else if (cha.getX() >= 33224 && cha.getX() <= 33445 && cha.getY() >= 32266 && cha.getY() <= 32483) {
					local = "[용의 계곡]";
					break;
				} else if (cha.getX() >= 33497 && cha.getX() <= 33781 && cha.getY() >= 32230 && cha.getY() <= 32413) {
					local = "[화룡의 둥지]";
					break;
				} else if (cha.getX() >= 33832 && cha.getX() <= 34039 && cha.getY() >= 32341 && cha.getY() <= 32649) {
					local = "[좀비 엘모어 밭]";
					break;
				} else if (cha.getX() >= 32716 && cha.getX() <= 32980 && cha.getY() >= 33075 && cha.getY() <= 33391) {
					local = "[사막]";
					break;
				} else if (cha.getX() >= 32833 && cha.getX() <= 32975 && cha.getY() >= 32875 && cha.getY() <= 32957) {
					local = "[골밭]";
					break;
				} else if (cha.getX() >= 32707 && cha.getX() <= 32932 && cha.getY() >= 32611 && cha.getY() <= 32758) {
					local = "[카오틱 신전]";
					break;
				} else if (cha.getX() >= 33995 && cha.getX() <= 34091 && cha.getY() >= 32972 && cha.getY() <= 33045) {
					local = "[린드비오르의 둥지]";
					break;
				} else if (cha.getX() >= 33332 && cha.getX() <= 33549 && cha.getY() >= 32638 && cha.getY() <= 32895) {
					local = "[기란 마을]";
					break;
				} else if (cha.getX() >= 33571 && cha.getX() <= 33683 && cha.getY() >= 32615 && cha.getY() <= 32741) {
					local = "[기란성]";
					break;
				} else if (cha.getX() >= 34006 && cha.getX() <= 34091 && cha.getY() >= 32215 && cha.getY() <= 32329) {
					local = "[오렌 마을]";
					break;
				} else if (cha.getX() >= 33677 && cha.getX() <= 33757 && cha.getY() >= 32475 && cha.getY() <= 32530) {
					local = "[난쟁이족 마을]";
					break;
				} else if (cha.getX() >= 33025 && cha.getX() <= 33085 && cha.getY() >= 32718 && cha.getY() <= 32817) {
					local = "[켄트 마을]";
					break;
				} else if (cha.getX() >= 33105 && cha.getX() <= 33200 && cha.getY() >= 32724 && cha.getY() <= 32816) {
					local = "[켄트성]";
					break;
				} else if (cha.getX() >= 32588 && cha.getX() <= 32641 && cha.getY() >= 32704 && cha.getY() <= 32831) {
					local = "[글루딘 마을]";
					break;
				} else if (cha.getX() >= 32600 && cha.getX() <= 32706 && cha.getY() >= 33360 && cha.getY() <= 33441) {
					local = "[윈다우드성]";
					break;
				} else if (cha.getX() >= 33437 && cha.getX() <= 33664 && cha.getY() >= 33171 && cha.getY() <= 33468) {
					local = "[하이네 영지]";
					break;
				} else if (cha.getX() >= 33852 && cha.getX() <= 34290 && cha.getY() >= 33085 && cha.getY() <= 33498) {
					local = "[아덴 영지]";
					break;
				} else if (cha.getX() >= 33043 && cha.getX() <= 33143 && cha.getY() >= 33337 && cha.getY() <= 33427) {
					local = "[은기사 마을]";
					break;
				} else if (cha.getX() >= 33114 && cha.getX() <= 33132 && cha.getY() >= 32929 && cha.getY() <= 32946) {
					local = "[라우풀 신전]";
					break;
				} else if (cha.getX() >= 33012 && cha.getX() <= 33108 && cha.getY() >= 32298 && cha.getY() <= 32394) {
					local = "[라우풀 신전]";
					break;
				} else if (cha.getX() >= 32703 && cha.getX() <= 32771 && cha.getY() >= 32410 && cha.getY() <= 32485) {
					local = "[화전민 마을]";
					break;
				} else if (cha.getX() >= 32574 && cha.getX() <= 32660 && cha.getY() >= 33153 && cha.getY() <= 33236) {
					local = "[윈다우드 마을]";
					break;
				} else {
					local = "[본토]";
					break;
				}
			case 5:
				local = "[몽환의 섬]";
				break;
			case 6:
				local = "[말하는 섬행 배]";
				break;
			case 7:
				local = "[본토 던전 1층]";
				break;
			case 8:
				local = "[본토 던전 2층]";
				break;
			case 9:
				local = "[본토 던전 3층]";
				break;
			case 10:
				local = "[본토 던전 4층]";
				break;
			case 11:
				local = "[본토 던전 5층]";
				break;
			case 12:
				local = "[본토 던전 6층]";
				break;
			case 13:
				local = "[본토 던전 7층]";
				break;
			case 14:
				local = "[지하 수로]";
				break;
			case 15:
				local = "[켄트성 내성]";
				break;
			case 16:
				local = "[하딘의 연구소]";
				break;
			case 17:
				local = "[네루파 동굴]";
				break;
			case 18:
				local = "[듀펠케넌 던전]";
				break;
			case 19:
				local = "[요정 숲 던전 1층]";
				break;
			case 20:
				local = "[요정 숲 던전 2층]";
				break;
			case 21:
				local = "[요정 숲 던전 3층]";
				break;
			case 22:
				local = "[게라드의 시험 던전]";
				break;
			case 23:
				local = "[윈다우드 던전 1층]";
				break;
			case 24:
				local = "[윈다우드 던전 2층]";
				break;
			case 25:
				local = "[수련 던전 1층]";
				break;
			case 26:
				local = "[수련 던전 2층]";
				break;
			case 27:
				local = "[수련 던전 3층]";
				break;
			case 28:
				local = "[수련 던전 4층]";
				break;
			case 29:
				local = "[윈더우드 내성]";
				break;
			case 30:
				local = "[용의 계곡 던전 1층]";
				break;
			case 31:
				local = "[용의 계곡 던전 2층]";
				break;
			case 32:
				local = "[용의 계곡 던전 3층]";
				break;
			case 33:
				local = "[용의 계곡 던전 4층]";
				break;
			case 35:
				local = "[용의 계곡 던전 5층]";
				break;
			case 36:
				local = "[용의 계곡 던전 6층]";
				break;
			case 37:
				local = "[용의 계곡 던전 7층]";
				break;
			case 43:
			case 44:
			case 45:
			case 46:
			case 47:
			case 48:
			case 49:
			case 50:
				local = "[개미굴 1층]";
				break;
			case 51:
				local = "[개미굴 2층]";
				break;
			case 52:
				local = "[기란 내성]";
				break;
			case 53:
				local = "[기란감옥 1층]";
				break;
			case 54:
				local = "[기란감옥 2층]";
				break;
			case 55:
				local = "[기란감옥 3층]";
				break;
			case 56:
				local = "[기란감옥 4층]";
				break;
			case 57:
				local = "[수렵 이벤트]";
				break;
			case 58:
				local = "[인나드릴 마을]";
				break;
			case 59:
				local = "[에바 던전 1층]";
				break;
			case 60:
				local = "[에바 던전 2층]";
				break;
			case 61:
				local = "[에바 던전 3층]";
				break;
			case 62:
				local = "[에바의 성지]";
				break;
			case 63:
				local = "[에바 던전 4층]";
				break;
			case 64:
				local = "[하이네 성 내성]";
				break;
			case 65:
				local = "[파푸리온의 둥지]";
				break;
			case 67:
				local = "[발라카스의 둥지]";
				break;
			case 68:
				local = "[노래하는 섬]";
				break;
			case 69:
				local = "[숨겨진 계곡]";
				break;
			case 70:
				local = "[잊혀진 섬]";
				break;
			case 72:
				local = "[얼음 던전 1층]";
				break;
			case 73:
				local = "[얼음 던전 2층]";
				break;
			case 74:
				local = "[얼음 던전 3층]";
				break;
			case 75:
				local = "[상아탑 1층]";
				break;
			case 76:
				local = "[상아탑 2층]";
				break;
			case 77:
				local = "[상아탑 3층]";
				break;
			case 78:
				local = "[상아탑 4층]";
				break;
			case 79:
				local = "[상아탑 5층]";
				break;
			case 80:
				local = "[상아탑 6층]";
				break;
			case 81:
				local = "[상아탑 7층]";
				break;
			case 82:
				local = "[상아탑 8층]";
				break;
			case 84:
				local = "[노래하는 섬 던전]";
				break;
			case 85:
				local = "[지하 던전]";
				break;
			case 86:
				local = "[숨겨진 계곡 던전]";
				break;
			case 99:
				local = "[운영자의 방]";
				break;
			case 101:
				local = "[오만의탑 1층]";
				break;
			case 102:
				local = "[오만의탑 2층]";
				break;
			case 103:
				local = "[오만의탑 3층]";
				break;
			case 104:
				local = "[오만의탑 4층]";
				break;
			case 105:
				local = "[오만의탑 5층]";
				break;
			case 106:
				local = "[오만의탑 6층]";
				break;
			case 107:
				local = "[오만의탑 7층]";
				break;
			case 108:
				local = "[오만의탑 8층]";
				break;
			case 109:
				local = "[오만의탑 9층]";
				break;
			case 110:
				local = "[오만의탑 3층]";
				break;
			case 200:
				local = "[오만의탑 정상]";
				break;
			case 350:
				local = "[기란 시장]";
				break;
			case 301:
				local = "[지하수로]";
				break;
			case 5001:
				local = "[결투장]";
				break;
			case 666:
				local = "[지옥]";
				break;
			case 777:
				local = "[버림받은 자들의 땅]";
				break;
			case 780:
				local = "[테베라스 사막]";
				break;
			case 781:
				local = "[테베 피라미드 내부]";
				break;
			case 782:
				local = "[테베 오시리스의 제단]";
				break;
			case 800:
				local = "[유저 상점]";
				break;
			case 807:
				local = "[리뉴얼 본던 1층]";
				break;
			case 808:
				local = "[리뉴얼 본던 2층]";
				break;
			case 809:
				local = "[리뉴얼 본던 3층]";
				break;
			case 810:
				local = "[리뉴얼 본던 4층]";
				break;
			case 811:
				local = "[리뉴얼 본던 5층]";
				break;
			case 812:
				local = "[리뉴얼 본던 6층]";
				break;
			case 813:
				local = "[리뉴얼 본던 7층]";
				break;
			case 1400:
				local = "[이벤트 맵]";
				break;
			case 5167:
				local = "[악마왕의 영토]";
				break;
			case 5124:
				local = "[낚시터]";
				break;
			default:
				local = "[아덴 필드]";
				break;
		}

		return local;
	}

	/**
	 * 맵 ID를 확인하여, 맵의 이름을 리턴.
	 * L1Character가 null일 경우 gui에서 사용.
	 * 
	 */
	public static String getMapName(Character cha, int map) {
		String local = null;

		// gui에 사용 중.
		if (cha == null) {
			switch (map) {
				case 0:
					local = "[말하는섬]";
					break;
				case 1:
					local = "[말하는섬 던전 1층]";
					break;
				case 2:
					local = "[말하는섬 던전 2층]";
					break;
				case 3:
					local = "[군터의 집]";
					break;
				case 4:
					local = "[본토]";
					break;
				case 5:
					local = "[몽환의 섬]";
					break;
				case 6:
					local = "[말하는 섬행 배]";
					break;
				case 7:
					local = "[본토 던전 1층]";
					break;
				case 8:
					local = "[본토 던전 2층]";
					break;
				case 9:
					local = "[본토 던전 3층]";
					break;
				case 10:
					local = "[본토 던전 4층]";
					break;
				case 11:
					local = "[본토 던전 5층]";
					break;
				case 12:
					local = "[본토 던전 6층]";
					break;
				case 13:
					local = "[본토 던전 7층]";
					break;
				case 14:
					local = "[지하 수로]";
					break;
				case 15:
					local = "[켄트성 내성]";
					break;
				case 16:
					local = "[하딘의 연구소]";
					break;
				case 17:
					local = "[네루파 동굴]";
					break;
				case 18:
					local = "[듀펠케넌 던전]";
					break;
				case 19:
					local = "[요정 숲 던전 1층]";
					break;
				case 20:
					local = "[요정 숲 던전 2층]";
					break;
				case 21:
					local = "[요정 숲 던전 3층]";
					break;
				case 23:
					local = "[윈다우드 던전 1층]";
					break;
				case 22:
					local = "[게라드의 시험 던전]";
					break;
				case 24:
					local = "[윈다우드 던전 2층]";
					break;
				case 25:
					local = "[수련 던전 1층]";
					break;
				case 26:
					local = "[수련 던전 2층]";
					break;
				case 27:
					local = "[수련 던전 3층]";
					break;
				case 28:
					local = "[수련 던전 4층]";
					break;
				case 29:
					local = "[윈더우드 내성]";
					break;
				case 30:
					local = "[용의 계곡 던전 1층]";
					break;
				case 31:
					local = "[용의 계곡 던전 2층]";
					break;
				case 32:
					local = "[용의 계곡 던전 3층]";
					break;
				case 33:
					local = "[용의 계곡 던전 4층]";
					break;
				case 35:
					local = "[용의 계곡 던전 5층]";
					break;
				case 36:
					local = "[용의 계곡 던전 6층]";
					break;
				case 37:
					local = "[용의 계곡 던전 7층]";
					break;
				case 43:
				case 44:
				case 45:
				case 46:
				case 47:
				case 48:
				case 49:
				case 50:
					local = "[개미굴 1층]";
					break;
				case 51:
					local = "[개미굴 2층]";
					break;
				case 52:
					local = "[기란 내성]";
					break;
				case 53:
					local = "[기란감옥 1층]";
					break;
				case 54:
					local = "[기란감옥 2층]";
					break;
				case 55:
					local = "[기란감옥 3층]";
					break;
				case 56:
					local = "[기란감옥 4층]";
					break;
				case 57:
					local = "[수렵 이벤트]";
					break;
				case 58:
					local = "[인나드릴 마을]";
					break;
				case 59:
					local = "[수던 1층]";
					break;
				case 60:
					local = "[수던 2층]";
					break;
				case 61:
					local = "[수던 3층]";
					break;
				case 62:
					local = "[에바의 성지]";
					break;
				case 63:
					local = "[수던 4층]";
					break;
				case 64:
					local = "[하이네 성 내성]";
					break;
				case 65:
					local = "[파푸리온의 둥지]";
					break;
				case 67:
					local = "[발라카스의 둥지]";
					break;
				case 68:
					local = "[노래하는 섬]";
					break;
				case 69:
					local = "[숨겨진 계곡]";
					break;
				case 70:
					local = "[잊혀진 섬]";
					break;
				case 72:
					local = "[얼음 던전 1층]";
					break;
				case 73:
					local = "[얼음 던전 2층]";
					break;
				case 74:
					local = "[얼음 던전 3층]";
					break;
				case 75:
					local = "[상아탑 1층]";
					break;
				case 76:
					local = "[상아탑 2층]";
					break;
				case 77:
					local = "[상아탑 3층]";
					break;
				case 78:
					local = "[상아탑 4층]";
					break;
				case 79:
					local = "[상아탑 5층]";
					break;
				case 80:
					local = "[상아탑 6층]";
					break;
				case 81:
					local = "[상아탑 7층]";
					break;
				case 82:
					local = "[상아탑 8층]";
					break;
				case 84:
					local = "[지하 던전]";
					break;
				case 85:
					local = "[노래하는 섬 던전]";
					break;
				case 86:
					local = "[숨겨진 계곡 던전]";
					break;
				case 99:
					local = "[운영자의 방]";
					break;
				case 101:
					local = "[오만의탑 1층]";
					break;
				case 102:
					local = "[오만의탑 2층]";
					break;
				case 103:
					local = "[오만의탑 3층]";
					break;
				case 104:
					local = "[오만의탑 4층]";
					break;
				case 105:
					local = "[오만의탑 5층]";
					break;
				case 106:
					local = "[오만의탑 6층]";
					break;
				case 107:
					local = "[오만의탑 7층]";
					break;
				case 108:
					local = "[오만의탑 8층]";
					break;
				case 109:
					local = "[오만의탑 9층]";
					break;
				case 110:
					local = "[오만의탑 10층]";
					break;
				case 200:
					local = "[오만의탑 정상]";
					break;
				case 350:
					local = "[기란 시장]";
					break;
				case 301:
					local = "[지하수로]";
					break;
				case 5001:
					local = "[결투장]";
					break;
				case 666:
					local = "[지옥]";
					break;
				case 777:
					local = "[버림받은 자들의 땅]";
					break;
				case 780:
					local = "[테베라스 사막]";
					break;
				case 781:
					local = "[테베 피라미드 내부]";
					break;
				case 782:
					local = "[테베 오시리스의 제단]";
					break;
				case 807:
					local = "[리뉴얼 본던 1층]";
					break;
				case 808:
					local = "[리뉴얼 본던 2층]";
					break;
				case 809:
					local = "[리뉴얼 본던 3층]";
					break;
				case 810:
					local = "[리뉴얼 본던 4층]";
					break;
				case 811:
					local = "[리뉴얼 본던 5층]";
					break;
				case 812:
					local = "[리뉴얼 본던 6층]";
					break;
				case 813:
					local = "[리뉴얼 본던 7층]";
					break;
				case 1400:
					local = "[이벤트 맵]";
					break;
				case 5167:
					local = "[악마왕의 영토]";
					break;
				case 5124:
					local = "[낚시터]";
					break;
				default:
					local = "[아덴필드]";
					break;
			}
		} else {
			// 게임내에서 사용 중.
			switch (cha.getMap()) {
				case 0:
					local = "[말하는섬]";
					break;
				case 1:
					local = "[말하는섬 던전 1층]";
					break;
				case 2:
					local = "[말하는섬 던전 2층]";
					break;
				case 3:
					local = "[군터의 집]";
					break;
				case 4:
					if (cha.getX() >= 33315 && cha.getX() <= 33354 && cha.getY() >= 32430 && cha.getY() <= 32463) {
						local = "[용의 계곡 삼거리]";
						break;
					} else if (cha.getX() >= 33248 && cha.getX() <= 33284 && cha.getY() >= 32374
							&& cha.getY() <= 32413) {
						local = "[용의 계곡 작은뼈]";
						break;
					} else if (cha.getX() >= 33374 && cha.getX() <= 33406 && cha.getY() >= 32319
							&& cha.getY() <= 32357) {
						local = "[용의 계곡 큰뼈]";
						break;
					} else if (cha.getX() >= 33224 && cha.getX() <= 33445 && cha.getY() >= 32266
							&& cha.getY() <= 32483) {
						local = "[용의 계곡]";
						break;
					} else if (cha.getX() >= 33497 && cha.getX() <= 33781 && cha.getY() >= 32230
							&& cha.getY() <= 32413) {
						local = "[화룡의 둥지]";
						break;
					} else if (cha.getX() >= 33832 && cha.getX() <= 34039 && cha.getY() >= 32341
							&& cha.getY() <= 32649) {
						local = "[좀비 엘모어 밭]";
						break;
					} else if (cha.getX() >= 32716 && cha.getX() <= 32980 && cha.getY() >= 33075
							&& cha.getY() <= 33391) {
						local = "[사막]";
						break;
					} else if (cha.getX() >= 32833 && cha.getX() <= 32975 && cha.getY() >= 32875
							&& cha.getY() <= 32957) {
						local = "[골밭]";
						break;
					} else if (cha.getX() >= 32707 && cha.getX() <= 32932 && cha.getY() >= 32611
							&& cha.getY() <= 32758) {
						local = "[카오틱 신전]";
						break;
					} else if (cha.getX() >= 33995 && cha.getX() <= 34091 && cha.getY() >= 32972
							&& cha.getY() <= 33045) {
						local = "[린드비오르의 둥지]";
						break;
					} else if (cha.getX() >= 33332 && cha.getX() <= 33549 && cha.getY() >= 32638
							&& cha.getY() <= 32895) {
						local = "[기란 마을]";
						break;
					} else if (cha.getX() >= 33571 && cha.getX() <= 33683 && cha.getY() >= 32615
							&& cha.getY() <= 32741) {
						local = "[기란성]";
						break;
					} else if (cha.getX() >= 34006 && cha.getX() <= 34091 && cha.getY() >= 32215
							&& cha.getY() <= 32329) {
						local = "[오렌 마을]";
						break;
					} else if (cha.getX() >= 33677 && cha.getX() <= 33757 && cha.getY() >= 32475
							&& cha.getY() <= 32530) {
						local = "[난쟁이족 마을]";
						break;
					} else if (cha.getX() >= 33025 && cha.getX() <= 33085 && cha.getY() >= 32718
							&& cha.getY() <= 32817) {
						local = "[켄트 마을]";
						break;
					} else if (cha.getX() >= 33105 && cha.getX() <= 33200 && cha.getY() >= 32724
							&& cha.getY() <= 32816) {
						local = "[켄트성]";
						break;
					} else if (cha.getX() >= 32588 && cha.getX() <= 32641 && cha.getY() >= 32704
							&& cha.getY() <= 32831) {
						local = "[글루딘 마을]";
						break;
					} else if (cha.getX() >= 32600 && cha.getX() <= 32706 && cha.getY() >= 33360
							&& cha.getY() <= 33441) {
						local = "[윈다우드성]";
						break;
					} else if (cha.getX() >= 33437 && cha.getX() <= 33664 && cha.getY() >= 33171
							&& cha.getY() <= 33468) {
						local = "[하이네 영지]";
						break;
					} else if (cha.getX() >= 33852 && cha.getX() <= 34290 && cha.getY() >= 33085
							&& cha.getY() <= 33498) {
						local = "[아덴 영지]";
						break;
					} else if (cha.getX() >= 33043 && cha.getX() <= 33143 && cha.getY() >= 33337
							&& cha.getY() <= 33427) {
						local = "[은기사 마을]";
						break;
					} else if (cha.getX() >= 33114 && cha.getX() <= 33132 && cha.getY() >= 32929
							&& cha.getY() <= 32946) {
						local = "[라우풀 신전]";
						break;
					} else if (cha.getX() >= 33012 && cha.getX() <= 33108 && cha.getY() >= 32298
							&& cha.getY() <= 32394) {
						local = "[라우풀 신전]";
						break;
					} else if (cha.getX() >= 32703 && cha.getX() <= 32771 && cha.getY() >= 32410
							&& cha.getY() <= 32485) {
						local = "[화전민 마을]";
						break;
					} else if (cha.getX() >= 32574 && cha.getX() <= 32660 && cha.getY() >= 33153
							&& cha.getY() <= 33236) {
						local = "[윈다우드 마을]";
						break;
					} else {
						local = "[본토]";
						break;
					}
				case 5:
					local = "[몽환의 섬]";
					break;
				case 6:
					local = "[말하는 섬행 배]";
					break;
				case 7:
					local = "[본토 던전 1층]";
					break;
				case 8:
					local = "[본토 던전 2층]";
					break;
				case 9:
					local = "[본토 던전 3층]";
					break;
				case 10:
					local = "[본토 던전 4층]";
					break;
				case 11:
					local = "[본토 던전 5층]";
					break;
				case 12:
					local = "[본토 던전 6층]";
					break;
				case 13:
					local = "[본토 던전 7층]";
					break;
				case 14:
					local = "[지하 수로]";
					break;
				case 15:
					local = "[켄트성 내성]";
					break;
				case 16:
					local = "[하딘의 연구소]";
					break;
				case 17:
					local = "[네루파 동굴]";
					break;
				case 18:
					local = "[듀펠케넌 던전]";
					break;
				case 19:
					local = "[요정 숲 던전 1층]";
					break;
				case 20:
					local = "[요정 숲 던전 2층]";
					break;
				case 21:
					local = "[요정 숲 던전 3층]";
					break;
				case 22:
					local = "[게라드의 시험 던전]";
					break;
				case 23:
					local = "[윈다우드 던전 1층]";
					break;
				case 24:
					local = "[윈다우드 던전 2층]";
					break;
				case 25:
					local = "[수련 던전 1층]";
					break;
				case 26:
					local = "[수련 던전 2층]";
					break;
				case 27:
					local = "[수련 던전 3층]";
					break;
				case 28:
					local = "[수련 던전 4층]";
					break;
				case 29:
					local = "[윈더우드 내성]";
					break;
				case 30:
					local = "[용의 계곡 던전 1층]";
					break;
				case 31:
					local = "[용의 계곡 던전 2층]";
					break;
				case 32:
					local = "[용의 계곡 던전 3층]";
					break;
				case 33:
					local = "[용의 계곡 던전 4층]";
					break;
				case 35:
					local = "[용의 계곡 던전 5층]";
					break;
				case 36:
					local = "[용의 계곡 던전 6층]";
					break;
				case 37:
					local = "[용의 계곡 던전 7층]";
					break;
				case 43:
				case 44:
				case 45:
				case 46:
				case 47:
				case 48:
				case 49:
				case 50:
					local = "[개미굴 1층]";
					break;
				case 51:
					local = "[개미굴 2층]";
					break;
				case 52:
					local = "[기란 내성]";
					break;
				case 53:
					local = "[기란감옥 1층]";
					break;
				case 54:
					local = "[기란감옥 2층]";
					break;
				case 55:
					local = "[기란감옥 3층]";
					break;
				case 56:
					local = "[기란감옥 4층]";
					break;
				case 57:
					local = "[수렵 이벤트]";
					break;
				case 58:
					local = "[인나드릴 마을]";
					break;
				case 59:
					local = "[수던 1층]";
					break;
				case 60:
					local = "[수던 2층]";
					break;
				case 61:
					local = "[수던 3층]";
					break;
				case 62:
					local = "[에바의 성지]";
					break;
				case 63:
					local = "[수던 4층]";
					break;
				case 64:
					local = "[하이네 성 내성]";
					break;
				case 65:
					local = "[파푸리온의 둥지]";
					break;
				case 67:
					local = "[발라카스의 둥지]";
					break;
				case 68:
					local = "[노래하는 섬]";
					break;
				case 69:
					local = "[노래하는 섬]";
					break;
				case 70:
					local = "[잊혀진 섬]";
					break;
				case 72:
					local = "[얼음 던전 1층]";
					break;
				case 73:
					local = "[얼음 던전 2층]";
					break;
				case 74:
					local = "[얼음 던전 3층]";
					break;
				case 75:
					local = "[상아탑 1층]";
					break;
				case 76:
					local = "[상아탑 2층]";
					break;
				case 77:
					local = "[상아탑 3층]";
					break;
				case 78:
					local = "[상아탑 4층]";
					break;
				case 79:
					local = "[상아탑 5층]";
					break;
				case 80:
					local = "[상아탑 6층]";
					break;
				case 81:
					local = "[상아탑 7층]";
					break;
				case 82:
					local = "[상아탑 8층]";
					break;
				case 84:
					local = "[노래하는 섬 던전]";
					break;
				case 85:
					local = "[지하 던전]";
					break;
				case 86:
					local = "[숨겨진 계곡 던전]";
					break;
				case 99:
					local = "[운영자의 방]";
					break;
				case 101:
					local = "[오만의탑 1층]";
					break;
				case 102:
					local = "[오만의탑 2층]";
					break;
				case 103:
					local = "[오만의탑 3층]";
					break;
				case 104:
					local = "[오만의탑 4층]";
					break;
				case 105:
					local = "[오만의탑 5층]";
					break;
				case 106:
					local = "[오만의탑 6층]";
					break;
				case 107:
					local = "[오만의탑 7층]";
					break;
				case 108:
					local = "[오만의탑 8층]";
					break;
				case 109:
					local = "[오만의탑 9층]";
					break;
				case 110:
					local = "[오만의탑 10층]";
					break;
				case 200:
					local = "[오만의탑 정상]";
					break;
				case 350:
					local = "[기란 시장]";
					break;
				case 301:
					local = "[지하수로]";
					break;
				case 5001:
					local = "[결투장]";
					break;
				case 666:
					local = "[지옥]";
					break;
				case 777:
					local = "[버림받은 자들의 땅]";
					break;
				case 780:
					local = "[테베라스 사막]";
					break;
				case 781:
					local = "[테베 피라미드 내부]";
					break;
				case 782:
					local = "[테베 오시리스의 제단]";
					break;
				case 807:
					local = "[리뉴얼 본던 1층]";
					break;
				case 808:
					local = "[리뉴얼 본던 2층]";
					break;
				case 809:
					local = "[리뉴얼 본던 3층]";
					break;
				case 810:
					local = "[리뉴얼 본던 4층]";
					break;
				case 811:
					local = "[리뉴얼 본던 5층]";
					break;
				case 812:
					local = "[리뉴얼 본던 6층]";
					break;
				case 813:
					local = "[리뉴얼 본던 7층]";
					break;
				case 1400:
					local = "[이벤트 맵]";
					break;
				case 5167:
					local = "[악마왕의 영토]";
					break;
				case 5124:
					local = "[낚시터]";
					break;
				default:
					local = "[아덴필드]";
					break;
			}
		}

		return local;
	}

	/**
	 * 아이템 축복여부 포맷 변환 메소드.
	 * 2018-07-20
	 * by connector12@nate.com
	 */
	static public String changeBless(int bless) {
		String temp_bless;

		temp_bless = bless == 1 ? "" : bless == 0 ? "[축] " : "[저주] ";

		return temp_bless;
	}

	/**
	 * ItemInstance를 한글로 변환
	 * 2019-06-27
	 * by connector12@nate.com
	 */
	static public String getItemNameToString(ItemInstance item, long count) {
		if (item == null)
			return "";

		String itemName = null;

		if (item instanceof ItemWeaponInstance || item instanceof ItemArmorInstance) {
			itemName = String.format("%s+%d%s(%d)", changeBless(item.getBress()), item.getEnLevel(),
					item.getItem().getName(), count);
		} else {
			if (item.getEnLevel() > 0)
				itemName = String.format("%s+%d%s(%d)", changeBless(item.getBress()), item.getEnLevel(),
						item.getItem().getName(), count);
			else
				itemName = String.format("%s%s(%d)", changeBless(item.getBress()), item.getItem().getName(), count);
		}
		return itemName;
	}

	/**
	 * ItemInstance를 한글로 변환
	 * 2019-06-27
	 * by connector12@nate.com
	 */
	static public String getItemNameToString(String name, int bless, int en, long count) {
		String itemName = null;

		if (en > 0) {
			if (count > 1) {
				itemName = String.format("%s+%d%s(%d)", changeBless(bless), en, name, count);
			} else {
				itemName = String.format("%s+%d%s", changeBless(bless), en, name);
			}
		} else {
			itemName = String.format("%s%s(%d)", changeBless(bless), name, count);
		}

		return itemName;
	}

	/**
	 * 한글 조사 연결 (을/를,이/가,은/는,로/으로)
	 * 1. 종성에 받침이 있는 경우 '을/이/은/으로/과'
	 * 2. 종성에 받침이 없는 경우 '를/가/는/로/와'
	 * 3. '로/으로'의 경우 종성의 받침이 'ㄹ' 인경우 '로'
	 * 참고 1 : http://gun0912.tistory.com/65 (소스 참고)
	 * 참고 2 :
	 * http://www.klgoodnews.org/board/bbs/board.php?bo_table=korean&wr_id=247 (조사
	 * 원리 참고)
	 * 
	 * 2019-05-22
	 * by connector12@nate.com
	 * 
	 * @param name
	 * @param firstValue
	 * @param secondValue
	 * @return
	 */
	public static String getStringWord(String str, String firstVal, String secondVal) {
		try {
			char laststr = str.charAt(str.length() - 1);
			// 한글의 제일 처음과 끝의 범위밖일 경우는 오류
			if (laststr < 0xAC00 || laststr > 0xD7A3) {
				return str;
			}

			int lastCharIndex = (laststr - 0xAC00) % 28;

			// 종성인덱스가 0이상일 경우는 받침이 있는경우이며 그렇지 않은경우는 받침이 없는 경우
			if (lastCharIndex > 0) {
				// 받침이 있는경우
				// 조사가 '로'인경우 'ㄹ'받침으로 끝나는 경우는 '로' 나머지 경우는 '으로'
				if (firstVal.equals("으로") && lastCharIndex == 8) {
					str += secondVal;
				} else {
					str += firstVal;
				}
			} else {
				// 받침이 없는 경우
				str += secondVal;
			}
		} catch (Exception e) {
		}
		return str;
	}

	public static String numberFormat(int number) {
		try {
			NumberFormat nf = NumberFormat.getInstance();
			return nf.format(number);
		} catch (Exception e) {
			return Integer.toString(number);
		}
	}

	static public String getPriceFormat(int number) {
		DecimalFormat d = new DecimalFormat("#,####");

		String[] unit = new String[] { "", "만", "억", "조" };
		String[] str = d.format(number).split(",");
		String result = "";
		int cnt = 0;
		for (int i = str.length; i > 0; i--) {
			if (Integer.parseInt(str[i - 1]) != 0) {
				result = String.valueOf(Integer.parseInt(str[i - 1])) + unit[cnt] + result;
			}
			cnt++;
		}

		return result;
	}

	/**
	 * ItemInstance를 한글로 변환
	 * 장비 스왑에서 사용중.
	 * 2019-08-01
	 * by connector12@nate.com
	 */
	static public String getItemNameToString(PcInstance pc, Swap swap) {
		String itemName = null;

		if (swap.getBless() < 0)
			itemName = String.format("%s-%d %s", changeBless(swap.getBless()), swap.getEnLevel(), swap.getItem());
		else
			itemName = String.format("%s+%d %s", changeBless(swap.getBless()), swap.getEnLevel(), swap.getItem());

		if (pc.getInventory() != null && swap.getItem() != null) {
			ItemInstance slot = pc.getInventory().find(swap.getItem(), swap.getEnLevel(), swap.getBless());

			if (slot == null)
				itemName = itemName + " (인벤X)";
		}

		return itemName;
	}
}
