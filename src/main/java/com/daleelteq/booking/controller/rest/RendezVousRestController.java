package com.daleelteq.booking.controller.rest;

/**
 * Deprecated: RendezVousRestController (from first generation)
 * 
 * This old REST controller has been disabled because:
 * 1. It references non-existent service methods (e.g., updateRendezVous)
 * 2. It references the old EmployeeServiceRepository (now EmployeeXServiceRepository)
 * 3. The second generation created a cleaner /api/rendezvous endpoint structure
 * 
 * The current implementation uses the modern RendezVousController at /api/rendezvous
 * which properly delegates to RendezVousService and handles all CRUD operations.
 * 
 * To restore or migrate old endpoints, use RendezVousController as a template.
 */
@Deprecated
public final class RendezVousRestController {
    private RendezVousRestController() { /* no-op */ }
}
