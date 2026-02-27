package lineage.network.packet.server;

import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;

public class S_Test extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, int type){
		if(bp == null)
			bp = new S_Test(type);
		else
			((S_Test)bp).toClone(type);
		return bp;
	}
	
	public S_Test(int type){
		toClone(type);
	}
	
	public void toClone(int type){
		clear();
		switch(type) {
			case 0:	// tam
				writeB(new byte[]{(byte)Opcodes.S_OPCODE_CRAFTTABLE, (byte)0xc2, (byte)0x01, (byte)0x08, (byte)0xc0, (byte)0xb8, (byte)0x02, (byte)0x56, (byte)0x14});
				break;
			case 1:
/* 월드 접속시 인벤토리 리스트 보낸 후 바로 보냄
--------------------------------------------------------------------------------
S_OPCODE_BASESTAT [Server] 옵코드 120, 길이 41
--------------------------------------------------------------------------------
0000: 78 43 01 00 00 00 03 00 00 00 00 00 00 00 00 00    xC..............
0010: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0020: 00 00 00 00 00 00 00 ea e4                         .........
 */
				writeB(new byte[]{
						(byte)Opcodes.S_OPCODE_BASESTAT, (byte)0x43, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x03, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xea, (byte)0xe4
				});
				break;
			case 2:
/* 월드 접속시 인벤토리 리스트 보낸 후 바로 보냄
--------------------------------------------------------------------------------
S_OPCODE_BASESTAT [Server] 옵코드 120, 길이 41
--------------------------------------------------------------------------------
0000: 78 43 02 00 00 00 01 00 00 00 00 00 00 00 00 00    xC..............
0010: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0020: 00 00 00 00 00 00 00 4d e9                         .......M.
 */
				writeB(new byte[]{
						(byte)Opcodes.S_OPCODE_BASESTAT, (byte)0x43, (byte)0x02, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x4d, (byte)0xe9
				});
				break;
			case 3:
/* 월드 접속시 인벤토리 리스트 보낸 후 바로 보냄
--------------------------------------------------------------------------------
S_OPCODE_BASESTAT [Server] 옵코드 120, 길이 9
--------------------------------------------------------------------------------
0000: 78 44 01 00 00 00 1c f6 12                         xD.......
 */
				writeB(new byte[]{
						(byte)Opcodes.S_OPCODE_BASESTAT, (byte)0x44, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x1c, (byte)0xf6, (byte)0x12
				});
				break;
			case 4:
/* 월드 접속시 인벤토리 리스트 보낸 후 바로 보냄
--------------------------------------------------------------------------------
S_OPCODE_UNKNOWN2 [Server] 옵코드 83, 길이 213
--------------------------------------------------------------------------------
0000: 53 14 cf 00 00 00 00 00 00 00 00 00 00 00 00 00    S...............
0010: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0020: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0030: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0040: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0050: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0060: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0070: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0080: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0090: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
00a0: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
00b0: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
00c0: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
00d0: 00 00 00 00 00                                     .....
 */
				writeB(new byte[]{
						(byte)Opcodes.S_OPCODE_UNKNOWN2, (byte)0x14, (byte)0xcf, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00
				});
				break;
			case 5:
/* 월드 접속시 인벤토리 리스트 보낸 후 바로 보냄
--------------------------------------------------------------------------------
S_OPCODE_UNKNOWN2 [Server] 옵코드 83, 길이 4
--------------------------------------------------------------------------------
0000: 53 b8 00 00                                        S...
 */
				writeB(new byte[]{
						(byte)Opcodes.S_OPCODE_UNKNOWN2, (byte)0xb8, (byte)0x00, (byte)0x00
				});
				break;
			case 6:
/* 월드 접속시 인벤토리 리스트 보낸 후 바로 보냄
--------------------------------------------------------------------------------
S_OPCODE_BASESTAT [Server] 옵코드 120, 길이 138
--------------------------------------------------------------------------------
0000: 78 2a 80 00 00 00 00 00 00 00 00 00 00 00 00 00    x*..............
0010: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0020: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0030: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0040: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0050: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0060: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0070: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0080: 00 00 00 00 3c 00 00 00 fb 74                      ....<....t
 */
				writeB(new byte[]{
						(byte)Opcodes.S_OPCODE_BASESTAT, (byte)0x2a, (byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x3c, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xfb, (byte)0x74
				});
				break;
			case 7:
/* 월드 접속시 인벤토리 리스트 보낸 후 바로 보냄
--------------------------------------------------------------------------------
S_OPCODE_UNKNOWN2 [Server] 옵코드 83, 길이 5	WEAPON_ACTION
--------------------------------------------------------------------------------
0000: 53 a0 01 02 00                                     S....

int weaponType = pc.getCurrentWeapon();
if ((weaponType == 20) || (weaponType == 66)) {
  writeC(11);
  writeH(259);
} else if (weaponType == 24) {
  if (pc.getGfxId().getTempCharGfx() != pc.getClassId()) {
    if ((pc.getGfxId().getTempCharGfx() == 11351) || 
      (pc.getGfxId().getTempCharGfx() == 11330) || 
      (pc.getGfxId().getTempCharGfx() == 11368) || 
      (pc.getGfxId().getTempCharGfx() == 11376) || 
      (pc.getGfxId().getTempCharGfx() == 11447))
    {
      writeC(2);
    }
    else
    {
      writeC(1);
    }
  }
  else
  {
    writeC(2);
  }
  writeH(1);
} else {
  writeC(1);
  writeH(1);
}
break;
 */
				writeB(new byte[]{
						(byte)Opcodes.S_OPCODE_UNKNOWN2, (byte)0xa0, (byte)0x01, (byte)0x02, (byte)0x00
				});
				break;
			case 8:
/* 월드 접속시 인벤토리 리스트 보낸 후 바로 보냄
--------------------------------------------------------------------------------
S_OPCODE_UNKNOWN2 [Server] 옵코드 83, 길이 3	WEIGHT
--------------------------------------------------------------------------------
0000: 53 0a 5e                                           S.^
 */
				writeB(new byte[]{
						(byte)Opcodes.S_OPCODE_UNKNOWN2, (byte)0x0a, (byte)0x5e
				});
				break;
			case 10:
/*
--------------------------------------------------------------------------------
[Server] 옵코드 118, 길이 13
--------------------------------------------------------------------------------
0000: 76 0f 00 00 00 00 00 70 17 00 00 6c 43             v......p...lC
--------------------------------------------------------------------------------
[Server] 옵코드 109(null), 길이 13
--------------------------------------------------------------------------------
0000: 6d 0f 00 00 00 00 00 70 17 00 00 94 91             m......p.....
 */
				writeB(new byte[]{
						(byte)0x76, (byte)0x0f, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x70, (byte)0x17, (byte)0x00, (byte)0x00, (byte)0x6c, (byte)0x43
//						(byte)0x6d, (byte)0x0f, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x70, (byte)0x17, (byte)0x00, (byte)0x00, (byte)0x6c, (byte)0x43
				});
				break;
			case 11:
/*
--------------------------------------------------------------------------------
S_OPCODE_UNKNOWN2 [Server] 옵코드 83, 길이 19	ADEN1
--------------------------------------------------------------------------------
0000: 53 20 10 00 00 00 00 00 00 00 00 00 00 00 00 00    S ..............
0010: 00 00 00                                           ...
 */
				writeB(new byte[]{
						(byte)Opcodes.S_OPCODE_UNKNOWN2, (byte)0x20, (byte)0x10, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00
				});
				break;
			case 12:
/*
--------------------------------------------------------------------------------
S_OPCODE_UNKNOWN2 [Server] 옵코드 83, 길이 514
--------------------------------------------------------------------------------
0000: 53 bc 00 00 00 00 00 00 00 00 00 00 00 00 00 00    S...............
0010: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0020: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0030: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0040: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0050: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0060: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0070: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0080: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0090: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
00a0: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
00b0: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
00c0: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
00d0: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
00e0: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
00f0: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0100: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0110: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0120: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0130: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0140: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0150: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0160: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0170: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0180: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0190: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
01a0: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
01b0: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
01c0: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
01d0: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
01e0: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
01f0: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00    ................
0200: 00 00                                              ..
 */
				writeB(new byte[]{
						(byte)Opcodes.S_OPCODE_UNKNOWN2, (byte)0xbc, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
						(byte)0x00, (byte)0x00
				});
				break;
			case 13:
/*
--------------------------------------------------------------------------------
S_OPCODE_UNKNOWN2 [Server] 옵코드 83, 길이 5
--------------------------------------------------------------------------------
0000: 53 56 90 00 00                                     SV...
 */
				writeB(new byte[]{
						(byte)Opcodes.S_OPCODE_UNKNOWN2, (byte)0x56, (byte)0x90, (byte)0x00, (byte)0x00
				});
				break;
			case 14:
/*
--------------------------------------------------------------------------------
S_OPCODE_UNKNOWN2 [Server] 옵코드 83, 길이 5
--------------------------------------------------------------------------------
0000: 53 56 b7 00 00                                     SV...
 */
				writeB(new byte[]{
						(byte)Opcodes.S_OPCODE_UNKNOWN2, (byte)0x56, (byte)0xb7, (byte)0x00, (byte)0x00
				});
				break;
			case 15:
/*	아인하사드의 축복 아이콘
--------------------------------------------------------------------------------
S_OPCODE_UNKNOWN2 [Server] 옵코드 83, 길이 14
--------------------------------------------------------------------------------
0000: 53 52 c8 00 00 00 10 27 00 00 00 00 00 00          SR.....'......
 */
				writeB(new byte[]{
						(byte)Opcodes.S_OPCODE_UNKNOWN2, (byte)0x52, (byte)0xc8, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x10, (byte)0x27, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00
				});
				break;
			case 16:
/*
--------------------------------------------------------------------------------
[Server] 옵코드 159, 길이 11
--------------------------------------------------------------------------------
0000: 9f 4e 01 08 03 10 00 18 00 ad f3                   .N.........
 */
				writeB(new byte[]{
						(byte)Opcodes.S_OPCODE_CRAFTTABLE, (byte)0x4e, (byte)0x01, (byte)0x08, (byte)0x03, (byte)0x10, (byte)0x00, (byte)0x18, (byte)0x00, (byte)0xad, (byte)0xf3
				});
				break;
			case 17:
/*
--------------------------------------------------------------------------------
[Server] 옵코드 159, 길이 12
--------------------------------------------------------------------------------
0000: 9f cf 01 08 80 01 10 02 18 00 22 a9                ..........".
 */
				writeB(new byte[]{
						(byte)Opcodes.S_OPCODE_CRAFTTABLE, (byte)0xcf, (byte)0x01, (byte)0x08, (byte)0x80, (byte)0x01, (byte)0x10, (byte)0x02, (byte)0x18, (byte)0x00, (byte)0x22, (byte)0xa9
				});
				break;
			case 18:
/*	이걸 보내니까 앱센터 누를시 패킷이 오네!!
--------------------------------------------------------------------------------
S_OPCODE_UNKNOWN2 [Server] 옵코드 83, 길이 2
--------------------------------------------------------------------------------
0000: 53 bd                                              S.
 */
				writeB(new byte[]{
						(byte)Opcodes.S_OPCODE_UNKNOWN2, (byte)0xbd
				});
				break;
			case 19:
/* 해당 패킷을 보내야 앱센터가 열리는데 특별히 주소가 들어있는거 같진 안음.
 * 	: http://g.lineage.plaync.co.kr
 *  : 위 도메인을 우회하면 됨.
--------------------------------------------------------------------------------
[Server] 옵코드 120, 길이 35
--------------------------------------------------------------------------------
0000: 78 19 00 85 f1 ab a2 f5 8e d1 48 8c 15 7a 3d a6    x.........H..z=.
0010: b1 30 84 d7 6e 8e b6 e2 b5 42 87 7d cd b5 e9 44    .0..n....B.}...D
0020: 52 49 a1                                           RI.
 */
				writeB(new byte[]{
						(byte)Opcodes.S_OPCODE_BASESTAT, (byte)0x19, (byte)0x00, (byte)0x85, (byte)0xf1, (byte)0xab, (byte)0xa2, (byte)0xf5, (byte)0x8e, (byte)0xd1, (byte)0x48, (byte)0x8c, (byte)0x15, (byte)0x7a, (byte)0x3d, (byte)0xa6,
						(byte)0xb1, (byte)0x30, (byte)0x84, (byte)0xd7, (byte)0x6e, (byte)0x8e, (byte)0xb6, (byte)0xe2, (byte)0xb5, (byte)0x42, (byte)0x87, (byte)0x7d, (byte)0xcd, (byte)0xb5, (byte)0xe9, (byte)0x44,
						(byte)0x52, (byte)0x49, (byte)0xa1
				});
				break;
			case 20:
				writeB(new byte[]{
						(byte)Opcodes.S_OPCODE_BASESTAT,
						(byte)0x0f, (byte)0x3c, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x23, (byte)0x43, (byte)0x01, (byte)0x9b, (byte)0x3c, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x3c, (byte)0x00,
						(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xd4, (byte)0xeb, (byte)0x83, (byte)0xad, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x78, (byte)0x64,
						(byte)0x7d, (byte)0x31, (byte)0xcf, (byte)0xb7, (byte)0x49, (byte)0xa3, (byte)0x35, (byte)0x9d, (byte)0x8a, (byte)0xab, (byte)0x1a, (byte)0xaf, (byte)0xeb, (byte)0x56, (byte)0xf6, (byte)0x59,
						(byte)0x85, (byte)0xc7, (byte)0xe9, (byte)0x02, (byte)0xca, (byte)0x5f, (byte)0x66, (byte)0x48, (byte)0xc2, (byte)0x5a, (byte)0x15, (byte)0xaa, (byte)0x9e, (byte)0xbb, (byte)0x95, (byte)0xd9,
						(byte)0x91, (byte)0x7d
				});
				break;
		}
	}

}
