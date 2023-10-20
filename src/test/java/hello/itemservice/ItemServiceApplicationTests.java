package hello.itemservice;

import hello.itemservice.domain.item.Item;
import hello.itemservice.web.validation.Form.ItemSaveForm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ItemServiceApplicationTests {
	
	private static Item getItem(ItemSaveForm form) { // form -> item Repository
		Item item = new Item();
		item.setItemName(form.getItemName());
		item.setPrice(form.getPrice());
		item.setQuantity(form.getQuantity());
		return item;
	}


	@Test
	void contextLoads() {
	}

	@Test
	void Test(){
		Item item = new Item();
		ItemSaveForm saveitem = new ItemSaveForm();

		Object objectItem = new ItemSaveForm();

		System.out.println("objectItem = " + objectItem);

		/*System.out.println("Item.class.isAssignableFrom(item.getClass()); = "
				+ Item.class.isAssignableFrom(saveitem.getClass()));*/
	}

}
