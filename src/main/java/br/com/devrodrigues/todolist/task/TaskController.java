package br.com.devrodrigues.todolist.task;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel tasksModel, HttpServletRequest request) {
        System.out.println("Chegou no controller");
        var idUser = request.getAttribute("idUser");
        tasksModel.setIdUser((UUID) idUser);

        var currentDate = LocalDateTime.now();
        if(currentDate.isAfter(tasksModel.getStartAt()) || currentDate.isAfter(tasksModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data de inicio / data de término deve ser maior que a data atual");
        }

        if(tasksModel.getStartAt().isAfter(tasksModel.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data de inicio deve ser menor do que a data de termino");
        }

        var task = this.taskRepository.save(tasksModel);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        var tasks = this.taskRepository.findByIdUser((UUID) idUser);
        return tasks;
    }

    @PutMapping("/{id}")
    public TaskModel update(@RequestBody TaskModel taskModel, @PathVariable UUID id, HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        System.out.printf("idUser" + idUser);
        taskModel.setId((UUID) idUser);
        taskModel.setId(id);
        return this.taskRepository.save(taskModel);
    }

}
