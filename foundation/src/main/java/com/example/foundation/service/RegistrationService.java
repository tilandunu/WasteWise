package com.example.foundation.service;

import com.example.foundation.dto.request.RegisterPremisesRequest;
import com.example.foundation.dto.response.PremisesResponse;
import com.example.foundation.model.Bin;
import com.example.foundation.model.Premises;
import com.example.foundation.model.Tag;
import com.example.foundation.model.User;
import com.example.foundation.model.Zone;
import com.example.foundation.repository.BinRepository;
import com.example.foundation.repository.PremisesRepository;
import com.example.foundation.repository.TagRepository;
import com.example.foundation.repository.UserRepository;
import com.example.foundation.repository.ZoneRepository;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

        private final UserRepository userRepo;
        private final ZoneRepository zoneRepo;
        private final BinRepository binRepo;
        private final TagRepository tagRepo;
        private final PremisesRepository premisesRepo;

        public RegistrationService(UserRepository userRepo, ZoneRepository zoneRepo,
                        BinRepository binRepo, TagRepository tagRepo,
                        PremisesRepository premisesRepo) {
                this.userRepo = userRepo;
                this.zoneRepo = zoneRepo;
                this.binRepo = binRepo;
                this.tagRepo = tagRepo;
                this.premisesRepo = premisesRepo;
        }

        public PremisesResponse registerPremises(RegisterPremisesRequest request) {

                // 1️⃣ Validate User
                User owner = userRepo.findByUid(request.getOwnerUid())
                                .orElseThrow(() -> new RuntimeException("User not found"));

                // 2️⃣ Validate Zone
                Zone zone = zoneRepo.findById(request.getZoneId())
                                .orElseThrow(() -> new RuntimeException("Zone not found or not eligible"));

                // 3️⃣ Assign Bin
                Optional<Bin> availableBinOpt = binRepo.findByStatus("IN_STOCK")
                                .stream()
                                .filter(b -> b.getType().equalsIgnoreCase(request.getBinType()))
                                .findFirst();

                Bin bin = availableBinOpt
                                .orElseThrow(() -> new RuntimeException(
                                                "No available bins of type " + request.getBinType()));

                // 4️⃣ Pair Tag
                Tag tag = tagRepo.findByTagId(request.getTagId())
                                .orElseThrow(() -> new RuntimeException("Tag not found"));

                if (!tag.isActive()) {
                        throw new RuntimeException("Tag is inactive or already assigned");
                }

                // 5️⃣ Create Premises
                Premises premises = new Premises();
                premises.setAddress(request.getAddress());
                premises.setType(request.getType());
                premises.setContactNumber(request.getContactNumber());
                premises.setOwner(owner);
                premises.setZone(zone);

                // Save premises first to get ID
                premises = premisesRepo.save(premises);

                // 6️⃣ Link Bin and Tag
                bin.setStatus("ASSIGNED");
                bin.setPremises(premises);
                bin.setTag(tag);
                binRepo.save(bin);

                tag.setActive(false); // mark tag as assigned
                tagRepo.save(tag);

                // 7️⃣ Prepare Response DTO
                PremisesResponse response = new PremisesResponse();
                response.setPremisesId(premises.getId());
                response.setAddress(premises.getAddress());
                response.setType(premises.getType());
                response.setContactNumber(premises.getContactNumber());
                response.setZoneName(zone.getName());
                response.setOwnerName(owner.getName());

                PremisesResponse.BinResponse binResponse = new PremisesResponse.BinResponse();
                binResponse.setBinId(bin.getId());
                binResponse.setBinType(bin.getType());
                binResponse.setStatus(bin.getStatus());
                binResponse.setTagId(tag.getTagId());

                response.setBin(binResponse);

                return response;
        }
}