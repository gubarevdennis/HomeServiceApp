package den.gubarev.springproject.controllers.mainServiceREST;

import den.gubarev.springproject.dto.ResidentDTO;
import den.gubarev.springproject.models.Resident;
import den.gubarev.springproject.services.ResidentService;
import den.gubarev.springproject.util.Errors.ResidentErrorResponse;
import den.gubarev.springproject.util.Errors.ResidentNotCreatedException;
import den.gubarev.springproject.util.Errors.ResidentNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/resident")
public class ResidentRESTController {

    private final ResidentService residentService;
    private final ModelMapper modelMapper;

    @Autowired
    public ResidentRESTController(ModelMapper modelMapper, ResidentService residentService) {
        this.modelMapper = modelMapper;
        this.residentService = residentService;
    }

    @GetMapping
    public List<ResidentDTO> getResident() {
        return residentService.findAll().stream().map(this::convertToResidentDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResidentDTO show(@PathVariable("id") int id) {
        return convertToResidentDTO(residentService.findOne(id));
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid ResidentDTO residentDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();

            // Лист ошибок
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                // получаем ошибки полей и конкетинируем инфу
                errorMessage.append(error.getField())
                        .append("-")
                        .append(error.getDefaultMessage())
                        .append(";");
            }

            throw new ResidentNotCreatedException(errorMessage.toString());
        }

        residentService.save(convertToResident(residentDTO));

        // ответ с пустым телом и статусом 200
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ResidentErrorResponse> handleException(ResidentNotFoundException e) {
        ResidentErrorResponse residentErrorResponse = new ResidentErrorResponse(
                "No resident found with such id",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(residentErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ResidentErrorResponse> handleException(ResidentNotCreatedException e) {
        ResidentErrorResponse residentErrorResponse = new ResidentErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(residentErrorResponse, HttpStatus.BAD_REQUEST);
    }

    public Resident convertToResident(ResidentDTO residentDTO) {
        return modelMapper.map(residentDTO, Resident.class);
    }

    public ResidentDTO convertToResidentDTO(Resident resident) {
        return modelMapper.map(resident, ResidentDTO.class);
    }
}
