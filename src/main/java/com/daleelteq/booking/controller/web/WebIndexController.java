package com.daleelteq.booking.controller.web;

import com.daleelteq.booking.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class WebIndexController {

    private final ServiceRepository serviceRepository;
    private final EmployeeRepository employeeRepository;
    private final ClientRepository clientRepository;
    private final EmployeeXServiceRepository employeeXServiceRepository;
    private final RendezVousRepository rendezVousRepository;
    private final NotificationRepository notificationRepository;

    @GetMapping
    public String index(Model model) {
        // Populate lists for display
        model.addAttribute("services", serviceRepository.findAll());
        model.addAttribute("employees", employeeRepository.findAll());
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("employeeServices", employeeXServiceRepository.findAll());
        model.addAttribute("rendezvous", rendezVousRepository.findAll());
        model.addAttribute("notifications", notificationRepository.findAll());

        // Populate available IDs
        model.addAttribute("serviceIds", serviceRepository.findAll().stream()
                .map(s -> s.getId())
                .collect(Collectors.toList()));
        model.addAttribute("employeeIds", employeeRepository.findAll().stream()
                .map(e -> e.getId())
                .collect(Collectors.toList()));
        model.addAttribute("clientIds", clientRepository.findAll().stream()
                .map(c -> c.getId())
                .collect(Collectors.toList()));
        model.addAttribute("esIds", employeeXServiceRepository.findAll().stream()
                .map(es -> es.getId())
                .collect(Collectors.toList()));
        model.addAttribute("freeEsIds", employeeXServiceRepository.findByStatus("free").stream()
                .map(es -> es.getId())
                .collect(Collectors.toList()));
        model.addAttribute("rendezvousIds", rendezVousRepository.findAll().stream()
                .map(rv -> rv.getId())
                .collect(Collectors.toList()));
        model.addAttribute("notificationIds", notificationRepository.findAll().stream()
                .map(n -> n.getId())
                .collect(Collectors.toList()));

        return "index";
    }
}

