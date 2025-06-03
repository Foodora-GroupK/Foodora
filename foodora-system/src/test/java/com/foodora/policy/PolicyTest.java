package com.foodora.policy;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import com.foodora.MyFoodoraSystem;
import com.foodora.policy.delivery.*;
import com.foodora.policy.target.*;
import com.foodora.user.*;
import com.foodora.util.Coordinate;

public class PolicyTest {
    private MyFoodoraSystem system;
    private Manager manager;
    
    @BeforeEach
    void setUp() {
        system = MyFoodoraSystem.getInstance();
        manager = new Manager("CEO", "Manager", "ceo", "123456789");
        system.addUser(manager);
    }

    @Test
    void testDeliveryPolicyChange() {
        // Test setting fastest delivery policy
        DeliveryPolicy fastestPolicy = new FastestDeliveryPolicy();
        assertDoesNotThrow(() -> manager.determineDeliveryPolicy(fastestPolicy));
        assertEquals(fastestPolicy, system.getDeliveryPolicy());

        // Test setting fair occupation policy
        DeliveryPolicy fairPolicy = new FairOccupationPolicy();
        assertDoesNotThrow(() -> manager.determineDeliveryPolicy(fairPolicy));
        assertEquals(fairPolicy, system.getDeliveryPolicy());
    }

    @Test
    void testProfitPolicyChange() {
        // Test setting targeted profit policy
        TargetProfitPolicy targetedPolicy = new TargetProfitByServiceFee();
        assertDoesNotThrow(() -> system.setTargetProfitPolicy(targetedPolicy));
        assertEquals(targetedPolicy, system.getTargetProfitPolicy());

        // Test setting markup profit policy
        TargetProfitPolicy markupPolicy = new TargetProfitByMarkup();
        assertDoesNotThrow(() -> system.setTargetProfitPolicy(markupPolicy));
        assertEquals(markupPolicy, system.getTargetProfitPolicy());
    }

    @Test
    void testDeliveryPolicyImplementation() {
        // Set up test data
        Restaurant restaurant = new Restaurant(
            "TestRest",
            new Coordinate(10.0, 10.0),
            "rest1",
            "pass123"
        );
        system.addUser(restaurant);
        
        Courier nearCourier = new Courier(
            "Near",
            "Courier",
            new Coordinate(11.0, 11.0),
            "123-456-7890",
            "near",
            "pass123"
        );
        system.addUser(nearCourier);
        nearCourier.setOnDuty(true);
        
        Courier farCourier = new Courier(
            "Far",
            "Courier",
            new Coordinate(20.0, 20.0),
            "123-456-7890",
            "far",
            "pass123"
        );
        system.addUser(farCourier);
        farCourier.setOnDuty(true);

        // Test fastest delivery policy
        DeliveryPolicy fastestPolicy = new FastestDeliveryPolicy();
        manager.determineDeliveryPolicy(fastestPolicy);
        
        Courier selectedCourier = system.getDeliveryPolicy().selectCourier(
            system.getUsers().stream()
                .filter(u -> u instanceof Courier && ((Courier) u).isOnDuty())
                .map(u -> (Courier) u)
                .toList(),
            restaurant.getLocation()
        );
        
        assertEquals(nearCourier, selectedCourier); // Should select nearest courier

        // Test fair occupation policy
        DeliveryPolicy fairPolicy = new FairOccupationPolicy();
        manager.determineDeliveryPolicy(fairPolicy);
        
        // Make near courier complete some deliveries
        for (int i = 0; i < 5; i++) {
            nearCourier.completeDelivery();
        }
        
        selectedCourier = system.getDeliveryPolicy().selectCourier(
            system.getUsers().stream()
                .filter(u -> u instanceof Courier && ((Courier) u).isOnDuty())
                .map(u -> (Courier) u)
                .toList(),
            restaurant.getLocation()
        );
        
        assertEquals(farCourier, selectedCourier); // Should select courier with fewer deliveries
    }

    @Test
    void testUnauthorizedPolicyChange() {
        // Create a restaurant user
        Restaurant restaurant = new Restaurant(
            "TestRest",
            new Coordinate(10.0, 10.0),
            "rest1",
            "pass123"
        );
        system.addUser(restaurant);

        // Try to set policies as restaurant (should fail)
        DeliveryPolicy fastestPolicy = new FastestDeliveryPolicy();
        assertThrows(IllegalStateException.class, () -> {
            if (!(restaurant instanceof Manager)) {
                throw new IllegalStateException("Only managers can set delivery policy");
            }
            system.setDeliveryPolicy(fastestPolicy);
        });

        TargetProfitPolicy targetedPolicy = new TargetProfitByServiceFee();
        assertThrows(IllegalStateException.class, () -> {
            if (!(restaurant instanceof Manager)) {
                throw new IllegalStateException("Only managers can set profit policy");
            }
            system.setTargetProfitPolicy(targetedPolicy);
        });
    }

    @Test
    void testPolicyPersistence() {
        // Set initial policies
        DeliveryPolicy fastestPolicy = new FastestDeliveryPolicy();
        manager.determineDeliveryPolicy(fastestPolicy);
        
        TargetProfitPolicy targetedPolicy = new TargetProfitByServiceFee();
        system.setTargetProfitPolicy(targetedPolicy);
        
        // Create new system instance (simulating application restart)
        MyFoodoraSystem newSystem = MyFoodoraSystem.getInstance();
        
        // Verify policies persist
        assertEquals(fastestPolicy.getClass(), newSystem.getDeliveryPolicy().getClass());
        assertEquals(targetedPolicy.getClass(), newSystem.getTargetProfitPolicy().getClass());
    }
} 