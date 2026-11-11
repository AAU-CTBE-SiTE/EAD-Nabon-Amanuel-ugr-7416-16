package com.craft.crud;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ActorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllAndGetById() throws Exception {
        mockMvc.perform(get("/api/actors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Snow"))
                .andExpect(jsonPath("$[0].age").value(30))
                .andExpect(jsonPath("$[0].nationality").value("British"));

        mockMvc.perform(get("/api/actors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Snow"));

        mockMvc.perform(get("/api/actors/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateUpdatePatchDeleteAndIdNonConflict() throws Exception {
        // POST create actor (gets ID 2)
        mockMvc.perform(post("/api/actors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Emma Watson\",\"age\":34,\"nationality\":\"British\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Emma Watson"));

        // POST another actor (gets ID 3)
        mockMvc.perform(post("/api/actors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Idris Elba\",\"age\":51,\"nationality\":\"British\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3));

        // PUT update actor 2
        mockMvc.perform(put("/api/actors/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Emma Charlotte Watson\",\"age\":35,\"nationality\":\"English\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Emma Charlotte Watson"))
                .andExpect(jsonPath("$.age").value(35))
                .andExpect(jsonPath("$.nationality").value("English"));

        // PATCH partially update actor 1 (only age)
        mockMvc.perform(patch("/api/actors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"age\":31}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Snow"))
                .andExpect(jsonPath("$.age").value(31))
                .andExpect(jsonPath("$.nationality").value("British"));

        // DELETE actor 2 (middle element) and confirm shifting does not break retrieval
        mockMvc.perform(delete("/api/actors/2"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/actors/2"))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/actors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(3));

        // Create new actor after deletion -> must get ID 4 (no conflict with ID 2 or 3)
        mockMvc.perform(post("/api/actors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Cillian Murphy\",\"age\":48,\"nationality\":\"Irish\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(4));

        // Nonexistent IDs on PUT, PATCH, DELETE return 404
        mockMvc.perform(put("/api/actors/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Ghost\",\"age\":20,\"nationality\":\"Unknown\"}"))
                .andExpect(status().isNotFound());

        mockMvc.perform(patch("/api/actors/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Ghost\"}"))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/actors/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testArrayResizingBeyondInitialCapacity() throws Exception {
        // Initial array capacity is 10 with 1 default actor; add 12 more to trigger resize
        for (int i = 2; i <= 13; i++) {
            mockMvc.perform(post("/api/actors")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\":\"Actor " + i + "\",\"age\":" + (20 + i) + ",\"nationality\":\"Ethiopian\"}"))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(i));
        }

        mockMvc.perform(get("/api/actors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(13)));
    }

    @Test
    void testInputValidation() throws Exception {
        mockMvc.perform(post("/api/actors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"age\":30,\"nationality\":\"British\"}"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(put("/api/actors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Snow\",\"age\":0,\"nationality\":\"British\"}"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(patch("/api/actors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"age\":-1}"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(patch("/api/actors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
