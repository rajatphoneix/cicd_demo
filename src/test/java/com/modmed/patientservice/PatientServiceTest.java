@Test
public void testGetPatientById() {
    when(patientRepo.findById(1L)).thenReturn(Optional.of(new Patient(1L, "John")));
    Patient patient = patientService.getPatientById(1L);
    assertEquals("John", patient.getName());
}
