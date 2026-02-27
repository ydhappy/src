/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package cholong.util;

/**
 * <p>
 * �Ƶ� �ð��� ��ȭ�� �ޱ� ���� Listener �������̽�.
 * </p>
 * <p>
 * �Ƶ� �ð��� ��ȭ�� �����ؾ� �� Ŭ������, �� �������̽��� ������ �� �־� ��� �޼ҵ带 ������ �� �������̽��� �����ϴ���, �����ϴ�
 * �޼ҵ常�� �������̵�(override) �� abstract Ŭ���� L1GameTimeAdapter�� Ȯ���Ѵ�.
 * </p>
 * <p>
 * �׷��� Ŭ�����κ��� �ۼ��� û���� ������Ʈ��, L1GameTimeClock�� addListener �޼ҵ带 �����
 * L1GameTimeClock�� ��ϵȴ�. �Ƶ� �ð� ��ȭ�� ������, ���� �ú��� ���� �ٲ���� ���� ��������.
 * </p>
 * <p>
 * �̷��� �޼ҵ��, L1GameTimeClock�� thread�󿡼� �����Ѵ�. �̷��� �޼ҵ��� ó���� �ð��� �ɷ��� ���, �ٸ�
 * û���ڿ��Է��� ������ ���� ���ɼ��� �ִ�. �Ϸ���� �ð��� �ʿ�� �ϴ� ó����, thread�� ��� �ϴ� �޼ҵ��� ȣ���� ���ԵǴ� ó����
 * �ǽ��ϴ� ����, ���ο��� ���Ӱ� thread�� �ۼ��� ó���� �ǽ��ؾ� �ϴ� ���̴�.
 * </p>
 * 
 */
public interface TimeListener {
	/**
	 * �Ƶ� �ð��� ���� �ٲ���� ���� �ҷ� ����.
	 * 
	 * @param time
	 *            �ֽ��� �Ƶ� �ð�
	 */
	public void onMonthChanged(BaseTime time);

	/**
	 * �Ƶ� �ð��� ���� �ٲ���� ���� �ҷ� ����.
	 * 
	 * @param time
	 *            �ֽ��� �Ƶ� �ð�
	 */
	public void onDayChanged(BaseTime time);

	/**
	 * �Ƶ� �ð��� �ð��� �ٲ���� ���� �ҷ� ����.
	 * 
	 * @param time
	 *            �ֽ��� �Ƶ� �ð�
	 */
	public void onHourChanged(BaseTime time);

	/**
	 * �Ƶ� �ð��� ���� �ٲ���� ���� �ҷ� ����.
	 * 
	 * @param time
	 *            �ֽ��� �Ƶ� �ð�
	 */
	public void onMinuteChanged(BaseTime time);
}
