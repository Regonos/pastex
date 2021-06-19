package pl.igormaculewicz.pastex;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.igormaculewicz.pastex.model.PasswordInputModel;
import pl.igormaculewicz.pastex.model.PasteInputModel;
import pl.igormaculewicz.pastex.model.PasteListModel;
import pl.igormaculewicz.pastex.model.PasteOutputModel;
import pl.igormaculewicz.pastex.service.PasteService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class HomeController {

  @GetMapping
  public String pasteView() {

    return "redirect:/paste/new";
  }
}
