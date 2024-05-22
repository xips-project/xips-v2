package cat.uvic.xips.bootstrap;


import cat.uvic.xips.entities.*;
import cat.uvic.xips.repositories.ProductRepository;
import cat.uvic.xips.repositories.RatingRepository;
import cat.uvic.xips.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;
    private final RatingRepository ratingRepository;

    public void initializeUsers(){

        List<User> users = List.of(
                User.builder()
                        .id(UUID.randomUUID())
                        .username("johndoe")
                        .password(passwordEncoder.encode("P@ssw0rd123!"))
                        .email("johndoe@example.com")
                        .role(Role.USER)
                        .userProfile(UserProfile.builder()
                                .birthdate(LocalDate.of(1990, 1, 1))
                                .address("123 Main St")
                                .zipCode("12345")
                                .country("USA")
                                .cityName("Anytown")
                                .build())
                        .build(),
                User.builder()
                        .id(UUID.randomUUID())
                        .username("janedoe")
                        .password(passwordEncoder.encode("S3cur3P@ss!"))
                        .email("janedoe@example.com")
                        .role(Role.ADMIN)
                        .userProfile(UserProfile.builder()
                                .birthdate(LocalDate.of(1985, 5, 15))
                                .address("456 Oak St")
                                .zipCode("67890")
                                .country("USA")
                                .cityName("Othertown")
                                .build())
                        .build(),
                User.builder()
                        .id(UUID.randomUUID())
                        .username("michael99")
                        .password(passwordEncoder.encode("P@ssword!23"))
                        .email("michael99@example.com")
                        .role(Role.USER)
                        .userProfile(UserProfile.builder()
                                .birthdate(LocalDate.of(1992, 9, 9))
                                .address("789 Pine St")
                                .zipCode("54321")
                                .country("Canada")
                                .cityName("Smalltown")
                                .build())
                        .build(),
                User.builder()
                        .id(UUID.randomUUID())
                        .username("sarahconnor")
                        .password(passwordEncoder.encode("T3rm1n@tor!"))
                        .email("sarah.connor@example.com")
                        .role(Role.ADMIN)
                        .userProfile(UserProfile.builder()
                                .birthdate(LocalDate.of(1979, 11, 12))
                                .address("123 Elm St")
                                .zipCode("23456")
                                .country("UK")
                                .cityName("Bigcity")
                                .build())
                        .build(),
                User.builder()
                        .id(UUID.randomUUID())
                        .username("johnsmith")
                        .password(passwordEncoder.encode("Pa$$w0rd!"))
                        .email("john.smith@example.com")
                        .role(Role.USER)
                        .userProfile(UserProfile.builder()
                                .birthdate(LocalDate.of(1988, 2, 2))
                                .address("456 Maple St")
                                .zipCode("67812")
                                .country("Australia")
                                .cityName("Metropolis")
                                .build())
                        .build(),
                User.builder()
                        .id(UUID.randomUUID())
                        .username("alicewonder")
                        .password(passwordEncoder.encode("W0nd3rL@nd!"))
                        .email("alice.wonder@example.com")
                        .role(Role.ADMIN)
                        .userProfile(UserProfile.builder()
                                .birthdate(LocalDate.of(1995, 7, 21))
                                .address("789 Birch St")
                                .zipCode("89012")
                                .country("USA")
                                .cityName("Fairytown")
                                .build())
                        .build(),
                User.builder()
                        .id(UUID.randomUUID())
                        .username("bobmarley")
                        .password(passwordEncoder.encode("R@staf@ri!"))
                        .email("bob.marley@example.com")
                        .role(Role.USER)
                        .userProfile(UserProfile.builder()
                                .birthdate(LocalDate.of(1945, 2, 6))
                                .address("12 Marley St")
                                .zipCode("33445")
                                .country("Jamaica")
                                .cityName("Kingston")
                                .build())
                        .build(),
                User.builder()
                        .id(UUID.randomUUID())
                        .username("charliebrown")
                        .password(passwordEncoder.encode("Ch@rlie!"))
                        .email("charlie.brown@example.com")
                        .role(Role.USER)
                        .userProfile(UserProfile.builder()
                                .birthdate(LocalDate.of(1970, 10, 10))
                                .address("123 Peanuts St")
                                .zipCode("90210")
                                .country("USA")
                                .cityName("Cartoonville")
                                .build())
                        .build(),
                User.builder()
                        .id(UUID.randomUUID())
                        .username("daisyduck")
                        .password(passwordEncoder.encode("D@isy!D@ck"))
                        .email("daisy.duck@example.com")
                        .role(Role.ADMIN)
                        .userProfile(UserProfile.builder()
                                .birthdate(LocalDate.of(1937, 12, 13))
                                .address("456 Disney St")
                                .zipCode("11223")
                                .country("USA")
                                .cityName("Duckburg")
                                .build())
                        .build(),
                User.builder()
                        .id(UUID.randomUUID())
                        .username("elvispresley")
                        .password(passwordEncoder.encode("Elv1s!"))
                        .email("elvis.presley@example.com")
                        .role(Role.USER)
                        .userProfile(UserProfile.builder()
                                .birthdate(LocalDate.of(1935, 1, 8))
                                .address("789 Graceland St")
                                .zipCode("56789")
                                .country("USA")
                                .cityName("Memphis")
                                .build())
                        .build(),
                User.builder()
                        .id(UUID.randomUUID())
                        .username("frankcastle")
                        .password(passwordEncoder.encode("Pun1sh3r!"))
                        .email("frank.castle@example.com")
                        .role(Role.ADMIN)
                        .userProfile(UserProfile.builder()
                                .birthdate(LocalDate.of(1974, 8, 18))
                                .address("123 Warzone St")
                                .zipCode("34567")
                                .country("USA")
                                .cityName("New York")
                                .build())
                        .build(),
                User.builder()
                        .id(UUID.randomUUID())
                        .username("gwenstacy")
                        .password(passwordEncoder.encode("Sp1der!"))
                        .email("gwen.stacy@example.com")
                        .role(Role.USER)
                        .userProfile(UserProfile.builder()
                                .birthdate(LocalDate.of(1993, 3, 3))
                                .address("456 Web St")
                                .zipCode("56789")
                                .country("USA")
                                .cityName("New York")
                                .build())
                        .build(),
                User.builder()
                        .id(UUID.randomUUID())
                        .username("hankpym")
                        .password(passwordEncoder.encode("AntM@n!"))
                        .email("hank.pym@example.com")
                        .role(Role.USER)
                        .userProfile(UserProfile.builder()
                                .birthdate(LocalDate.of(1960, 4, 4))
                                .address("789 Science St")
                                .zipCode("67890")
                                .country("USA")
                                .cityName("San Francisco")
                                .build())
                        .build(),
                User.builder()
                        .id(UUID.randomUUID())
                        .username("indianajones")
                        .password(passwordEncoder.encode("Ind1ana!"))
                        .email("indiana.jones@example.com")
                        .role(Role.ADMIN)
                        .userProfile(UserProfile.builder()
                                .birthdate(LocalDate.of(1942, 7, 1))
                                .address("123 Adventure St")
                                .zipCode("78901")
                                .country("USA")
                                .cityName("Chicago")
                                .build())
                        .build(),
                User.builder()
                        .id(UUID.randomUUID())
                        .username("jessicajones")
                        .password(passwordEncoder.encode("J3ssic@!"))
                        .email("jessica.jones@example.com")
                        .role(Role.USER)
                        .userProfile(UserProfile.builder()
                                .birthdate(LocalDate.of(1982, 2, 2))
                                .address("456 Alias St")
                                .zipCode("89012")
                                .country("USA")
                                .cityName("New York")
                                .build())
                        .build(),
                User.builder()
                        .id(UUID.randomUUID())
                        .username("kellykapoor")
                        .password(passwordEncoder.encode("K3lly!"))
                        .email("kelly.kapoor@example.com")
                        .role(Role.USER)
                        .userProfile(UserProfile.builder()
                                .birthdate(LocalDate.of(1981, 6, 5))
                                .address("789 Office St")
                                .zipCode("90123")
                                .country("USA")
                                .cityName("Scranton")
                                .build())
                        .build(),
                User.builder()
                        .id(UUID.randomUUID())
                        .username("laracroft")
                        .password(passwordEncoder.encode("T0mbR@ider!"))
                        .email("lara.croft@example.com")
                        .role(Role.ADMIN)
                        .userProfile(UserProfile.builder()
                                .birthdate(LocalDate.of(1975, 2, 14))
                                .address("123 Treasure St")
                                .zipCode("23456")
                                .country("UK")
                                .cityName("London")
                                .build())
                        .build(),
                User.builder()
                        .id(UUID.randomUUID())
                        .username("martyMcFly")
                        .password(passwordEncoder.encode("T1meTrav3l!"))
                        .email("marty.mcfly@example.com")
                        .role(Role.USER)
                        .userProfile(UserProfile.builder()
                                .birthdate(LocalDate.of(1968, 3, 3))
                                .address("456 DeLorean St")
                                .zipCode("34567")
                                .country("USA")
                                .cityName("Hill Valley")
                                .build())
                        .build(),
                User.builder()
                        .id(UUID.randomUUID())
                        .username("nancydrew")
                        .password(passwordEncoder.encode("D3tective!"))
                        .email("nancy.drew@example.com")
                        .role(Role.USER)
                        .userProfile(UserProfile.builder()
                                .birthdate(LocalDate.of(1930, 4, 28))
                                .address("789 Mystery St")
                                .zipCode("45678")
                                .country("USA")
                                .cityName("River Heights")
                                .build())
                        .build(),
                User.builder()
                        .id(UUID.randomUUID())
                        .username("oswaldcobblepot")
                        .password(passwordEncoder.encode("P3ngu1n!"))
                        .email("oswald.cobblepot@example.com")
                        .role(Role.ADMIN)
                        .userProfile(UserProfile.builder()
                                .birthdate(LocalDate.of(1970, 6, 15))
                                .address("123 Iceberg St")
                                .zipCode("56789")
                                .country("USA")
                                .cityName("Gotham City")
                                .build())
                        .build(),
                User.builder()
                        .id(UUID.randomUUID())
                        .username("peterparker")
                        .password(passwordEncoder.encode("Sp1derM@n!"))
                        .email("peter.parker@example.com")
                        .role(Role.USER)
                        .userProfile(UserProfile.builder()
                                .birthdate(LocalDate.of(1991, 8, 10))
                                .address("456 Webcrawler St")
                                .zipCode("67890")
                                .country("USA")
                                .cityName("New York")
                                .build())
                        .build(),
                User.builder()
                        .id(UUID.randomUUID())
                        .username("quentinbeck")
                        .password(passwordEncoder.encode("Myst3r1o!"))
                        .email("quentin.beck@example.com")
                        .role(Role.ADMIN)
                        .userProfile(UserProfile.builder()
                                .birthdate(LocalDate.of(1960, 9, 12))
                                .address("789 Illusion St")
                                .zipCode("78901")
                                .country("USA")
                                .cityName("Los Angeles")
                                .build())
                        .build(),
                User.builder()
                        .id(UUID.randomUUID())
                        .username("rickgrimes")
                        .password(passwordEncoder.encode("Z0mbieK1ll3r!"))
                        .email("rick.grimes@example.com")
                        .role(Role.USER)
                        .userProfile(UserProfile.builder()
                                .birthdate(LocalDate.of(1975, 9, 14))
                                .address("123 Survival St")
                                .zipCode("89012")
                                .country("USA")
                                .cityName("Atlanta")
                                .build())
                        .build(),
                User.builder()
                        .id(UUID.randomUUID())
                        .username("sherlockholmes")
                        .password(passwordEncoder.encode("D3dUct1v3!"))
                        .email("sherlock.holmes@example.com")
                        .role(Role.ADMIN)
                        .userProfile(UserProfile.builder()
                                .birthdate(LocalDate.of(1887, 1, 6))
                                .address("221B Baker St")
                                .zipCode("NW16XE")
                                .country("UK")
                                .cityName("London")
                                .build())
                        .build(),
                User.builder()
                        .id(UUID.randomUUID())
                        .username("tonystark")
                        .password(passwordEncoder.encode("1r0nM@n!"))
                        .email("tony.stark@example.com")
                        .role(Role.ADMIN)
                        .userProfile(UserProfile.builder()
                                .birthdate(LocalDate.of(1970, 5, 29))
                                .address("10880 Malibu Point")
                                .zipCode("90265")
                                .country("USA")
                                .cityName("Malibu")
                                .build())
                        .build(),
                User.builder()
                        .id(UUID.randomUUID())
                        .username("ursulamajor")
                        .password(passwordEncoder.encode("S3aw1tch!"))
                        .email("ursula.major@example.com")
                        .role(Role.USER)
                        .userProfile(UserProfile.builder()
                                .birthdate(LocalDate.of(1940, 10, 1))
                                .address("123 Ocean St")
                                .zipCode("56789")
                                .country("USA")
                                .cityName("Atlantica")
                                .build())
                        .build(),
                User.builder()
                        .id(UUID.randomUUID())
                        .username("victorvondoom")
                        .password(passwordEncoder.encode("D00m!"))
                        .email("victor.vondoom@example.com")
                        .role(Role.ADMIN)
                        .userProfile(UserProfile.builder()
                                .birthdate(LocalDate.of(1972, 12, 11))
                                .address("123 Castle Doom St")
                                .zipCode("78901")
                                .country("Latveria")
                                .cityName("Doomstadt")
                                .build())
                        .build(),
                User.builder()
                        .id(UUID.randomUUID())
                        .username("wade_wilson")
                        .password(passwordEncoder.encode("D3@dp00l!"))
                        .email("wade.wilson@example.com")
                        .role(Role.USER)
                        .userProfile(UserProfile.builder()
                                .birthdate(LocalDate.of(1975, 2, 21))
                                .address("789 Mutant St")
                                .zipCode("12345")
                                .country("USA")
                                .cityName("New York")
                                .build())
                        .build(),
                User.builder()
                        .id(UUID.randomUUID())
                        .username("xenonphoenix")
                        .password(passwordEncoder.encode("Ph03n1x!"))
                        .email("xenon.phoenix@example.com")
                        .role(Role.ADMIN)
                        .userProfile(UserProfile.builder()
                                .birthdate(LocalDate.of(1998, 11, 30))
                                .address("123 Solar St")
                                .zipCode("67890")
                                .country("USA")
                                .cityName("New York")
                                .build())
                        .build()
        );

        userRepository.saveAll(users);
    }





    public void initializeProducts(){

        List<String> usernames = List.of(
                "johndoe", "janedoe", "michael99", "sarahconnor", "johnsmith", "alicewonder", "bobmarley", "charliebrown",
                "daisyduck", "elvispresley", "frankcastle", "gwenstacy", "hankpym", "indianajones", "jessicajones",
                "kellykapoor", "laracroft", "martyMcFly", "nancydrew", "oswaldcobblepot", "peterparker", "quentinbeck",
                "rickgrimes", "sherlockholmes", "tonystark", "ursulamajor", "victorvondoom", "wade_wilson", "xenonphoenix"
        );

        List<Product> products = new ArrayList<>();
        ProductType[] productTypes = ProductType.values();

        for (int i = 0; i < 350; i++) {
            Product product = Product.builder()
                    .id(UUID.randomUUID())
                    .name("Product" + (i + 1))
                    .productType(productTypes[i % productTypes.length])
                    .username(usernames.get(i % usernames.size()))
                    .build();
            products.add(product);
        }

        productRepository.saveAll(products);
    }

    public void initializeRatings(){
        List<User> users = userRepository.findAll();
        List<Product> products =productRepository.findAll();
        ProductType[] productTypes = ProductType.values();

        List<Rating> ratings = new ArrayList<>();
        Map<Integer, String> starToMessageMap = Map.of(
                1, "Terrible",
                2, "Below expectations",
                3, "Average",
                4, "Very good",
                5, "Excellent product!"
        );


        for (User user : users) {
            for (int i = 1; i <= 80; i++) {
                int stars = (int) (Math.random() * 5) + 1;
                String message = starToMessageMap.get(stars);
                Rating rating = Rating.builder()
                        .id(UUID.randomUUID())
                        .stars(stars)
                        .message(message)
                        .user(user)
                        .build();
                ratings.add(rating);
            }
        }

        ratingRepository.saveAll(ratings);


    }



}
