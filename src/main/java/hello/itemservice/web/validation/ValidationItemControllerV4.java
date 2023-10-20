package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.web.validation.Form.ItemSaveForm;
import hello.itemservice.web.validation.Form.ItemUpdateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/validation/v4/items")
@RequiredArgsConstructor
@Slf4j
public class ValidationItemControllerV4 {

    private final ItemRepository itemRepository;

    private static Item form_to_item(ItemSaveForm form) { // form -> item Repository
        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setPrice(form.getPrice());
        item.setQuantity(form.getQuantity());
        return item;
    }

    private static Item form_to_item(ItemUpdateForm form) { // form -> item Repository
        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setPrice(form.getPrice());
        item.setQuantity(form.getQuantity());
        return item;
    }
    
    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v4/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v4/addForm";
    }

    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute("item") ItemSaveForm saveItemForm, BindingResult bindingResult, RedirectAttributes redirectAttributes , Model model) {

        resultprice(form_to_item(saveItemForm), bindingResult);

        if (bindingResult.hasErrors()) { // 검증실패시 다시 입력폼으로 되돌아감
            log.info("errors = {}",bindingResult);
            return "validation/v4/addForm";
        }

        //검증 완료 로직
        Item savedItem = itemRepository.save(form_to_item(saveItemForm));
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v4/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @Validated @ModelAttribute("item") ItemUpdateForm updateItemForm, BindingResult bindingResult) {

        resultprice(form_to_item(updateItemForm), bindingResult);

        if (bindingResult.hasErrors()) { // 검증실패시 다시 입력폼으로 되돌아감
            log.info("errors = {}",bindingResult);
            return "validation/v4/editForm";
        }

        itemRepository.update(itemId, form_to_item(updateItemForm));
        return "redirect:/validation/v4/items/{itemId}";
    }

    private static void resultprice(Item item, BindingResult bindingResult) {
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin",new Object[]{10000,resultPrice},null);
            }
        }
    }


}

