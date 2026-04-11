package edu.tmu.group67.scrum_development.tasks.service;

import edu.tmu.group67.scrum_development.auth.model.entity.User;
import edu.tmu.group67.scrum_development.enums.Status;
import edu.tmu.group67.scrum_development.sprintbacklog.model.entity.SprintBacklogItemEntity;
import edu.tmu.group67.scrum_development.sprintbacklog.repository.SprintBacklogItemRepository;
import edu.tmu.group67.scrum_development.tasks.model.entity.EngineeringTaskEntity;
import edu.tmu.group67.scrum_development.tasks.repository.EngineeringTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EngineeringTaskService {

    private final EngineeringTaskRepository taskRepository;
    private final SprintBacklogItemRepository sprintBacklogItemRepository;

    public List<EngineeringTaskEntity> getTasksForItem(Long sprintBacklogItemId) {
        return taskRepository.findBySprintBacklogItem_Id(sprintBacklogItemId);
    }

    public EngineeringTaskEntity createTask(Long sprintBacklogItemId, String title,
                                             String description, int effort, User createdBy) {
        SprintBacklogItemEntity item = sprintBacklogItemRepository.findById(sprintBacklogItemId)
                .orElseThrow(() -> new RuntimeException("Sprint backlog item not found"));

        EngineeringTaskEntity task = EngineeringTaskEntity.builder()
                .sprintBacklogItem(item)
                .title(title)
                .description(description)
                .effort(effort)
                .status(Status.IN_PROGRESS)
                .createdBy(createdBy)
                .build();

        return taskRepository.save(task);
    }

    public EngineeringTaskEntity updateTask(Long id, String title, String description,
                                             int effort, Status status) {
        EngineeringTaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setTitle(title);
        task.setDescription(description);
        task.setEffort(effort);
        task.setStatus(status);

        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found");
        }
        taskRepository.deleteById(id);
    }
}
