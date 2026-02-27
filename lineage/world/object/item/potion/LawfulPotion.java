package lineage.world.object.item.potion;

import lineage.network.packet.ClientBasePacket;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class LawfulPotion extends ItemInstance
{
  public static synchronized ItemInstance clone(ItemInstance paramItemInstance)
  {
    if (paramItemInstance == null)
      paramItemInstance = new LawfulPotion();
    return paramItemInstance;
  }

  public void toClick(Character paramCharacter, ClientBasePacket paramClientBasePacket)
  {
    if (!isClick(paramCharacter))
      return;
    if (getItem().getName().indexOf("라우풀") > -1){
		//paramCharacter.setLawful(98303);
    	paramCharacter.setLawful(98303);
		/*if(paramCharacter.getLawful()>95303){
			paramCharacter.setLawful(98303);
		}else{
			paramCharacter.setLawful(paramCharacter.getLawful()+3000);
		}*/
    }else if (getItem().getName().indexOf("카오틱") > -1){
    	paramCharacter.setLawful(0);
		/*if(paramCharacter.getLawful()<3000){
			paramCharacter.setLawful(0);
		}else{
			paramCharacter.setLawful(paramCharacter.getLawful()-3000);
		}*/
	}
    paramCharacter.getInventory().count(this, getCount() - 1L, true);
  }
}

/* Location:           D:\orim.jar
 * Qualified Name:     lineage.world.object.item.potion.LawfulPotion
 * JD-Core Version:    0.6.0
 */
