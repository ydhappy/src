package lineage.network.packet.server;

import java.sql.Connection;
import java.util.List;

import lineage.bean.database.Exp;
import lineage.database.DatabaseConnection;
import lineage.database.ExpDatabase;
import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;
import lineage.share.Lineage;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;
import lineage.world.object.item.Candle;
import lineage.world.object.item.DogCollar;
import lineage.world.object.item.Letter;
import lineage.world.object.item.pet.PetArmor;
import lineage.world.object.item.pet.PetWeapon;
import lineage.world.object.item.sp.CharacterSaveMarbles;
import lineage.world.object.item.sp.ExpSaveMarbles;
import lineage.world.object.item.wand.EbonyWand;
import lineage.world.object.item.wand.EbonyWand2;
import lineage.world.object.item.wand.MapleWand;
import lineage.world.object.item.wand.PineWand;
import lineage.world.object.item.weapon.Alice;

public class S_MarblesIvenInfo extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, List<ItemInstance> list) {
		if (bp == null)
			bp = new S_MarblesIvenInfo(list);
		else
			((S_MarblesIvenInfo) bp).toClone(list);
		return bp;
	}

	public S_MarblesIvenInfo(List<ItemInstance> list) {
		toClone(list);
	}

	public void toClone(List<ItemInstance> list) {
		clear();

		writeC(Opcodes.S_OPCODE_WAREHOUSE);
		writeD(0);
		writeH(list.size());
		writeC(3);
		readDB(list); // 창고 목록
	}

	private void readDB(List<ItemInstance> list) {
		for (ItemInstance wh : list) {
			int type = 0;
			if (wh instanceof ItemWeaponInstance)
				type = 1;
			else if (wh instanceof ItemArmorInstance)
				type = 2;
			else
				type = 3;

			writeD(wh.getObjectId()); // 번호
			writeC(type); // 타입
			writeH(wh.getItem().getInvGfx()); // GFX 아이디
			writeC(wh.getBress()); // 1: 보통 0: 축 2: 저주
			writeD(2000000000); // 구입희망 갯수를 무한으로 해주기위해 2억으로 표기
			writeC(wh.isDefinite() ? 1 : 0); // 1: 확인 0: 미확인

			if (wh instanceof ExpSaveMarbles || wh instanceof CharacterSaveMarbles) {
				Connection con = null;
				try {
					con = DatabaseConnection.getLineage();
					if (wh instanceof CharacterSaveMarbles) {
						CharacterSaveMarbles csm = (CharacterSaveMarbles) wh;
						csm.toWorldJoin(con, null);
					} else if (wh instanceof ExpSaveMarbles) {
						ExpSaveMarbles esm = (ExpSaveMarbles) wh;
						esm.toWorldJoin(con, null);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					DatabaseConnection.close(con);
				}
			}

			writeS(getName(wh)); // 이름
			if (Lineage.server_version >= 380)
				writeC(0x00);
		}
		writeD(0);
	}

	protected String getName(ItemInstance item) {
		StringBuffer sb = new StringBuffer();
		if (item.getItem().getNameIdNumber() == 1075 && item.getItem().getInvGfx() != 464) {
			Letter letter = (Letter) item;
			sb.append(letter.getFrom());
			sb.append(" : ");
			sb.append(letter.getSubject());
		} else {
			if (item.isDefinite() && (item instanceof ItemWeaponInstance || item instanceof ItemArmorInstance)) {
				// 속성 인첸 표현.
				String element_name = null;
				Integer element_en = 0;
				if (item.getEnWind() > 0) {
					element_name = "풍령";
					element_en = item.getEnWind();
				}
				if (item.getEnEarth() > 0) {
					element_name = "지령";
					element_en = item.getEnEarth();
				}
				if (item.getEnWater() > 0) {
					element_name = "수령";
					element_en = item.getEnWater();
				}
				if (item.getEnFire() > 0) {
					element_name = "화령";
					element_en = item.getEnFire();
				}
				if (element_name != null) {
					sb.append(element_name).append(":").append(element_en).append("단");
					sb.append(" ");
				}
				// 인첸 표현.
				if (item.getEnLevel() >= 0) {
					sb.append("+");
				}
				sb.append(item.getEnLevel());
				sb.append(" ");
			}
			// 앨리스
			if (item instanceof Alice)
				sb.append("(").append(((Alice) item).getLevel()).append(")");
			if (!(item instanceof CharacterSaveMarbles)) {
				if (item instanceof ExpSaveMarbles) {
					ExpSaveMarbles esm = (ExpSaveMarbles) item;
					if (esm.getExp() != 0) {
						sb.append("[" + esm.getSaveLevel() + "]저장된 경험치 구슬");
					} else {
						if (!item.getItem().getNameId().startsWith("$")) {
							sb.append(item.getName());
						} else {
							sb.append(item.isDefinite() && item.getItem().getNameIdCheck().length() > 0 ? item.getItem().getNameIdCheck()
									: item.getItem().getNameId());
						}
					}
				} else {

					if (!item.getItem().getNameId().startsWith("$")) {
						sb.append(item.getName());
					} else {
						sb.append(item.isDefinite() && item.getItem().getNameIdCheck().length() > 0 ? item.getItem().getNameIdCheck()
								: item.getItem().getNameId());
					}
				}
			}
			// 착용중인 아이템 표현
			if (item.isEquipped() && !(item instanceof PetArmor || item instanceof PetWeapon)) {
				if (item instanceof ItemWeaponInstance) {
					sb.append(" ($9)");
				} else if (item instanceof ItemArmorInstance) {
					sb.append(" ($117)");
				} else if (item instanceof Candle) {
					// 양초, 등잔
					sb.append(" ($10)");
				}
			}

			if (item.getCount() > 1) {
				sb.append(" (");
				sb.append(item.getCount());
				sb.append(")");
			}
			if (item.isDefinite() && (item instanceof MapleWand || item instanceof PineWand || item instanceof EbonyWand || item instanceof EbonyWand2)) {
				sb.append(" (");
				sb.append(item.getQuantity());
				sb.append(")");
			}
			if (item.getItem().getNameIdNumber() == 1173) {
				DogCollar dc = (DogCollar) item;
				sb.append(" [Lv.");
				sb.append(dc.getPetLevel());
				sb.append(" ");
				sb.append(dc.getPetName());
				sb.append("]");
			}
			if (item instanceof CharacterSaveMarbles) {
				CharacterSaveMarbles csm = (CharacterSaveMarbles) item;
				if (csm.getAccountUid() != 0) {
					String class_type = "";
					switch (csm.getCharacterClassType()) {
					case 0:
						class_type = "군주";
						break;
					case 1:
						class_type = "기사";
						break;
					case 2:
						class_type = "요정";
						break;
					case 3:
						class_type = "법사";
						break;
					case 4:
						class_type = "다크엘프";
						break;
					}
					sb.append(class_type + " (" + csm.getCharacterName() + ")");
					sb.append("\r\n");
					sb.append("[Lv." + csm.getCharacterLevel() + " ");
					double exp = 0.0;
					Exp exp_bean = ExpDatabase.find(csm.getCharacterLevel());
					if (exp_bean != null) {
						double e = csm.getCharacterExp() - (exp_bean.getBonus() - exp_bean.getExp());
						exp = (e / exp_bean.getExp()) * 100;
					}
					sb.append(String.format("%.2f%%", exp) + "]");
				} else {
					if (!item.getItem().getNameId().startsWith("$")) {
						sb.append(item.getName());
					} else {
						sb.append(item.isDefinite() && item.getItem().getNameIdCheck().length() > 0 ? item.getItem().getNameIdCheck()
								: item.getItem().getNameId());
					}
				}
			}
			if (item.getInnRoomKey() > 0) {
				sb.append(" #");
				sb.append(item.getInnRoomKey());
			}
		}

		return sb.toString().trim();
	}

}
