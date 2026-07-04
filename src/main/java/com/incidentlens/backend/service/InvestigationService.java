package com.incidentlens.backend.service;


import com.incidentlens.backend.entity.Investigation;
import com.incidentlens.backend.entity.InvestigationStatus;
import com.incidentlens.backend.entity.User;
import com.incidentlens.backend.repository.InvestigationRepository;
import com.incidentlens.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvestigationService {

    private final InvestigationRepository investigationRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    public InvestigationService(InvestigationRepository investigationRepository, UserRepository userRepository, AuthenticationService authenticationService) {
        this.investigationRepository = investigationRepository;
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
    }

    public Investigation createInvestigation(String title, String description){

        User currentUser = authenticationService.getCurrentUser();

        Investigation investigation = new Investigation();

        investigation.setTitle(title);
        investigation.setDescription(description);
        investigation.setStatus(InvestigationStatus.OPEN);
        investigation.setUser(currentUser);

        return investigationRepository.save(investigation);
    }

    public List<Investigation> getMyInvestigations(){

        User currentUser = authenticationService.getCurrentUser();

        return investigationRepository.findByUserOrderByCreatedAtDesc(currentUser);
    }

    public Investigation getInvestigationById(Long id){

        User currentUser = authenticationService.getCurrentUser();

        Investigation investigation = investigationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Investigation not found"));

        if(!investigation.getUser().getId().equals(currentUser.getId()))
            throw new RuntimeException("You do not have access to this resource.");

        return investigation;

    }

    public Investigation updateInvestigation(Long id, String title, String description, InvestigationStatus status){

        User currentUser = authenticationService.getCurrentUser();

        Investigation investigation = investigationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Investigation not found."));

        if(!investigation.getUser().getId().equals(currentUser.getId()))
            throw new RuntimeException("You do not have access to this investigation.");

        investigation.setTitle(title);
        investigation.setDescription(description);
        investigation.setStatus(status);

        return investigationRepository.save(investigation);
    }

    public void deleteInvestigation(Long id){

        User currentUser = authenticationService.getCurrentUser();

        Investigation investigation = investigationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Investigation not found."));

        if(!investigation.getUser().getId().equals(currentUser.getId()))
            throw new RuntimeException("You do not have access to this investigation.");

        investigationRepository.delete(investigation);
    }



}
