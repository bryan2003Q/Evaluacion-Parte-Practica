package com.practica.prueba;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

// =====================================================================
// ENTIDADES
// =====================================================================

@Entity
@Table(name = "properties")
@Getter @Setter @NoArgsConstructor
class Property {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String street;
    @Column(name = "monthly_price")
    private BigDecimal monthlyPrice;
    @Enumerated(EnumType.STRING)
    private PropertyStatus status;

    public Property(String name, String street, BigDecimal monthlyPrice) {
        this.name = name;
        this.street = street;
        this.monthlyPrice = monthlyPrice;
        this.status = PropertyStatus.AVAILABLE;
    }
}

enum PropertyStatus { AVAILABLE, OCCUPIED }

@Entity
@Table(name = "tenants")
@Getter @Setter @NoArgsConstructor
class Tenant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "full_name")
    private String fullName;
    private String email;
    private String phone;

    public Tenant(String fullName, String email, String phone) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
    }
}

@Entity
@Table(name = "rentals")
@Getter @Setter @NoArgsConstructor
class Rental {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "property_id")
    private Long propertyId;
    @Column(name = "tenant_id")
    private Long tenantId;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    private boolean active;

    public Rental(Long propertyId, Long tenantId) {
        this.propertyId = propertyId;
        this.tenantId = tenantId;
        this.startDate = LocalDate.now();
        this.active = true;
    }
}

// =====================================================================
// REPOSITORIOS
// =====================================================================

@Repository
interface PropertyRepo extends JpaRepository<Property, Long> {}

@Repository
interface TenantRepo extends JpaRepository<Tenant, Long> {}

@Repository
interface RentalRepo extends JpaRepository<Rental, Long> {
    List<Rental> findByActiveTrue();
}

// =====================================================================
// CONTROLADOR PRINCIPAL - TODO EN UN SOLO LUGAR
// =====================================================================

@Controller
@RequiredArgsConstructor
public class MainController {

    private final PropertyRepo propertyRepo;
    private final TenantRepo tenantRepo;
    private final RentalRepo rentalRepo;

    // --- DASHBOARD ---

    @GetMapping("/")
    public String dashboard(Model model) {
        List<Property> props = propertyRepo.findAll();
        long available = props.stream().filter(p -> p.getStatus() == PropertyStatus.AVAILABLE).count();
        model.addAttribute("totalProperties", props.size());
        model.addAttribute("availableProperties", available);
        model.addAttribute("occupiedProperties", props.size() - available);
        model.addAttribute("activeRentals", rentalRepo.findByActiveTrue().size());
        return "dashboard";
    }

    // --- PROPIEDADES ---

    @GetMapping("/properties")
    public String listProperties(Model model) {
        model.addAttribute("properties", propertyRepo.findAll());
        return "properties/list";
    }

    @GetMapping("/properties/new")
    public String newPropertyForm() {
        return "properties/form";
    }

    @PostMapping("/properties")
    public String createProperty(@RequestParam String name, @RequestParam String street, @RequestParam BigDecimal price) {
        propertyRepo.save(new Property(name, street, price));
        return "redirect:/properties";
    }

    @GetMapping("/properties/{id}/edit")
    public String editPropertyForm(@PathVariable Long id, Model model) {
        model.addAttribute("property", propertyRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Propiedad no encontrada")));
        return "properties/form";
    }

    @PostMapping("/properties/{id}")
    public String updateProperty(@PathVariable Long id, @RequestParam String name, @RequestParam String street, @RequestParam BigDecimal price) {
        Property p = propertyRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Propiedad no encontrada"));
        p.setName(name);
        p.setStreet(street);
        p.setMonthlyPrice(price);
        propertyRepo.save(p);
        return "redirect:/properties";
    }

    // --- INQUILINOS ---

    @GetMapping("/tenants")
    public String listTenants(Model model) {
        model.addAttribute("tenants", tenantRepo.findAll());
        return "tenants/list";
    }

    @GetMapping("/tenants/new")
    public String newTenantForm() {
        return "tenants/form";
    }

    @PostMapping("/tenants")
    public String createTenant(@RequestParam String fullName, @RequestParam String email, @RequestParam String phone) {
        tenantRepo.save(new Tenant(fullName, email, phone));
        return "redirect:/tenants";
    }

    // --- ARRIENDOS ---

    @GetMapping("/rentals")
    public String listRentals(Model model) {
        model.addAttribute("rentals", rentalRepo.findAll());
        model.addAttribute("properties", propertyRepo.findAll());
        model.addAttribute("tenants", tenantRepo.findAll());
        return "rentals/list";
    }

    @GetMapping("/rentals/new")
    public String newRentalForm(Model model) {
        model.addAttribute("properties", propertyRepo.findAll());
        model.addAttribute("tenants", tenantRepo.findAll());
        return "rentals/form";
    }

    @PostMapping("/rentals")
    public String createRental(@RequestParam Long propertyId, @RequestParam Long tenantId, Model model) {
        Property p = propertyRepo.findById(propertyId).orElseThrow(() -> new IllegalArgumentException("Propiedad no encontrada"));
        if (p.getStatus() == PropertyStatus.OCCUPIED) {
            model.addAttribute("error", "La propiedad no está disponible para arrendar.");
            model.addAttribute("properties", propertyRepo.findAll());
            model.addAttribute("tenants", tenantRepo.findAll());
            return "rentals/form";
        }
        p.setStatus(PropertyStatus.OCCUPIED);
        propertyRepo.save(p);
        rentalRepo.save(new Rental(propertyId, tenantId));
        return "redirect:/rentals";
    }

    @PostMapping("/rentals/{id}/finish")
    public String finishRental(@PathVariable Long id) {
        Rental r = rentalRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Arriendo no encontrado"));
        r.setActive(false);
        r.setEndDate(LocalDate.now());
        rentalRepo.save(r);
        Property p = propertyRepo.findById(r.getPropertyId()).orElseThrow();
        p.setStatus(PropertyStatus.AVAILABLE);
        propertyRepo.save(p);
        return "redirect:/rentals";
    }
}
