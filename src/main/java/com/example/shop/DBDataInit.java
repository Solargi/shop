package com.example.shop;

import com.example.shop.Embeddables.CartItemId;
import com.example.shop.models.Address;
import com.example.shop.models.CartItem;
import com.example.shop.models.Item;
import com.example.shop.models.User;
import com.example.shop.repositories.AddressRepository;
import com.example.shop.repositories.CartItemRepository;
import com.example.shop.repositories.ItemRepository;
import com.example.shop.repositories.UserRepository;
import com.example.shop.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
@Profile({"!prod"})
public class DBDataInit implements CommandLineRunner {
    private final UserRepository userRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;
    private final CartItemRepository cartItemRepository;
    private final AddressRepository addressRepository;
    @Override
    public void run(String... args) throws Exception {
        User u1 = new User();
        User u2 = new User();
//        User u3 = new User();


        u1.setId(1);
        u1.setName("a");
        u1.setSurname("b");
        u1.setUsername("u1");
        u1.setPassword("1");
        u1.setEmail("c");
        u1.setRoles("admin");
        u1.setBirthDate("yay");

//        u3.setId(3);
//        u3.setName("poop");
//        u3.setSurname("b");
//        u3.setUsername("u5");
//        u3.setPassword("q");
//        u3.setEmail("c");
//        u3.setRoles("admin");
//        u3.setBirthDate("yay");


        this.userService.save(u1);

        u2.setId(2);
        u2.setName("d");
        u2.setSurname("e");
        u2.setUsername("u2");
        u2.setPassword("2");
        u2.setEmail("g");
        u2.setRoles("user");
        u2.setBirthDate("yay2");
        //TODO: ASSIGN THEM TO ACTUAL OBJECTS
//
        Address a1 = new Address();
        Address a2 = new Address();
        a1.setId(1);
        a1.setCountry("a");
        a1.setCity("b");
        a1.setStreet("c");
        a1.setState("d");
        a1.setZipCode(56);
        a1.setUser(u1);

        a2.setId(3);
        a2.setCountry("e");
        a2.setCity("f");
        a2.setStreet("g");
        a2.setState("h");
        a2.setZipCode(57);
        u2.addAddress(a2);
        this.addressRepository.save(a1);
        this.userService.save(u2);
//        this.userRepository.save(u3);
        this.addressRepository.save(a1);

        Item i1 = new Item();
        Item i2 = new Item();
        Item i3 = new Item();
        Item i4 = new Item();
        Item i5 = new Item();


        i1.setId(1);
        i1.setName("a dasijhdiuashd dh asiudh saiudh saui usdhiasud hasuidh siasd hiasudh asiudh saiudh saiudh saiudh asuid hasuidh asiudh siaudh siaud hasui dhuias dhisuadh uiasdh uisahd ");
        i1.setCartItems(new ArrayList<>());
        i1.setOrderItems(new ArrayList<>());
        i1.setDescription("yay");
        i1.setPrice(new BigDecimal("32.1"));
        i1.setImageUrl("https://compote.slate.com/images/eb00a5c5-ba32-4ada-8b6e-ddf0fe6c350b.jpeg?crop=1560%2C1040%2Cx0%2Cy0");
        i1.setAvailableQuantity(new BigDecimal("40"));
//
//
        i2.setId(2);
        i2.setName("b");
        i2.setCartItems(new ArrayList<>());
        i2.setOrderItems(new ArrayList<>());
        i2.setDescription("soooooome long mushroom description.... i don't know what to write in it dhajkshdjksa" +
                "djklasdhjlaskdj" +
                "askldjlaskdj" +
                "skldjalksdj" +
                "lskjdlskajd" +
                "lskajdlkasjd" +
                "laksjdlksajd" +
                "laskjdl" +
                "askdjlsj]" +
                "jdkk" +
                "sjdksl" +
                "djksaldjsl" +
                "asjdlsdajl" +
                "");
        i2.setPrice(new BigDecimal("32.1"));
        i2.setImageUrl("https://www.seattlemet.com/discover/wp-content/uploads/2023/12/Amanita-mushroom-effects-152211545-1024x701.jpg");
        i2.setAvailableQuantity(new BigDecimal("40"));

        i3.setId(3);
        i3.setName("c");
        i3.setCartItems(new ArrayList<>());
        i3.setOrderItems(new ArrayList<>());
        i3.setDescription("soooooome long thing of things description.... i don't know what to write in it");
        i3.setPrice(new BigDecimal("32.1"));
        i3.setImageUrl("https://i.pinimg.com/236x/61/de/54/61de5494a3bc514197947da9ea66bd95.jpg");
        i3.setAvailableQuantity(new BigDecimal("40"));

        i4.setId(4);
        i4.setName("d");
        i4.setCartItems(new ArrayList<>());
        i4.setOrderItems(new ArrayList<>());
        i4.setDescription("soooooome long thing of things description.... i don't know what to write in it");
        i4.setPrice(new BigDecimal("32.1"));
        i4.setImageUrl("https://i5.walmartimages.com/asr/6e9f4769-0e07-4a28-a708-8b9e055044c9.5223902776abcbd6b92425187439c9e8.jpeg");
        i4.setAvailableQuantity(new BigDecimal("40"));

        i5.setId(5);
        i5.setName("e");
        i5.setCartItems(new ArrayList<>());
        i5.setOrderItems(new ArrayList<>());
        i5.setDescription("soooooome long thing of things description.... i don't know what to write in it");
        i5.setPrice(new BigDecimal("32.1"));
        i5.setImageUrl("https://images.unsplash.com/photo-1600185365483-26d7a4cc7519?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8OHx8c25lYWtlcnxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60");
        i5.setAvailableQuantity(new BigDecimal("40"));

        this.itemRepository.save(i1);
        this.itemRepository.save(i2);
        this.itemRepository.save(i3);
        this.itemRepository.save(i4);
        this.itemRepository.save(i5);

        CartItem ci1 = new CartItem();
        CartItem ci2 = new CartItem();
        CartItemId id1 = new CartItemId();
        CartItemId id3 = new CartItemId();
        CartItemId id2 = new CartItemId();

        List<CartItem> cartItemsList = new ArrayList<CartItem>();
        id1.setItemId(i1.getId());
        id1.setUserId(u1.getId());
        id3.setUserId(u2.getId());
        id3.setItemId(i2.getId());
        id2.setItemId(i2.getId());
        id2.setItemId(u1.getId());
//

        ci1.setId(id1);
        ci1.setItem(i1);
        ci1.setUser(u1);
        ci1.setQuantity(5);
        ci1.setTotalCost(ci1.computeTotalCost());

        ci2.setId(id3);
        ci2.setUser(u2);
        ci2.setItem(i2);
        ci2.setQuantity(2);
        ci2.setTotalCost(ci2.computeTotalCost());

//        List<CartItem> ciu1 = new ArrayList<>();
//        ciu1.add(ci1);
//        List<CartItem> ciu2 = new ArrayList<>();
//        ciu2.add(ci2);
//        ciu2.add(ci2);
        this.cartItemRepository.save(ci1);
        this.cartItemRepository.save(ci2);

    }
}
