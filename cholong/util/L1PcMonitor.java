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

import lineage.world.World;
import lineage.world.object.instance.PcInstance;

public abstract class L1PcMonitor implements Runnable {

	protected int _id;

	public L1PcMonitor(int oId) {
		_id = oId;
	}

	public int getObjectId() {
		return _id;
	}

	public final void run() {
		PcInstance pc = World.findPc(_id);
		if (pc == null || pc.getClient() == null) {
			return;
		}
		execTask(pc);
	}

	public abstract void execTask(PcInstance pc);
}
