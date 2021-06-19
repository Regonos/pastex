package pl.igormaculewicz.pastex;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.igormaculewicz.pastex.model.PasswordInputModel;
import pl.igormaculewicz.pastex.model.PasteInputModel;
import pl.igormaculewicz.pastex.model.PasteListModel;
import pl.igormaculewicz.pastex.model.PasteOutputModel;
import pl.igormaculewicz.pastex.service.PasteService;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
@RequestMapping("/paste")
public class PasteController {

  private final PasteService pasteService;

  @GetMapping("/{id}")
  public String pasteView(Model model,
                          HttpSession httpSession,
                          @PathVariable("id") UUID id) {

    if (pasteService.isPastePrivate(id) && Objects.isNull(httpSession.getAttribute(id.toString()))) {
      return "redirect:/paste/secured/" + id;
    }

    PasteOutputModel pasteModel = pasteService.getPaste(id);
    model.addAttribute("pasteModel", pasteModel);

    return "paste";
  }

  @GetMapping("/{id}/delete")
  public String deletePasteView(Model model,
                                @PathVariable("id") UUID id) {

    model.addAttribute("id", id);
    model.addAttribute("passwordModel", new PasswordInputModel());

    return "require_deletion_key";
  }

  @PostMapping("/{id}/delete")
  public String deletePasteAction(Model model, @ModelAttribute PasswordInputModel passwordInputModel, @PathVariable("id") UUID id) {

    if (pasteService.isDeletionKeyCorrect(id, parseUUIDSilently(passwordInputModel.getPassword()))) {
      pasteService.deletePaste(id);
      return "redirect:/paste/new";
    }

    model.addAttribute("id", id);
    model.addAttribute("passwordModel", new PasswordInputModel());
    model.addAttribute("error", "Wrong password");
    return "require_deletion_key";
  }

  @GetMapping("/secured/{id}")
  public String securedPasteView(Model model, @PathVariable("id") UUID id) {

    model.addAttribute("id", id);
    model.addAttribute("passwordModel", new PasswordInputModel());

    return "require_password";
  }

  @PostMapping("/secured/{id}")
  public String securedPasteAction(Model model, HttpSession httpSession, @ModelAttribute PasswordInputModel passwordInputModel, @PathVariable("id") UUID id) {

    if (pasteService.isPasswordCorrect(id, passwordInputModel.getPassword())) {
      httpSession.setAttribute(id.toString(), true);
      return "redirect:/paste/" + id;
    }

    model.addAttribute("id", id);
    model.addAttribute("passwordModel", new PasswordInputModel());
    model.addAttribute("error", "Wrong password");
    return "require_password";
  }

  @GetMapping("/new")
  public String newPasteView(Model model) {

    model.addAttribute("isNew", true);
    model.addAttribute("pasteModel", new PasteInputModel());

    return "paste";
  }

  @PostMapping("/new")
  public RedirectView newPasteAction(HttpSession httpSession, @ModelAttribute PasteInputModel pasteModel, RedirectAttributes redirectAttributes) {
    PasteOutputModel outputModel = pasteService.addPaste(pasteModel);

    httpSession.setAttribute(outputModel.getId().toString(), true);

    redirectAttributes.addFlashAttribute("deletionKey", outputModel.getDeletionKey());

    return new RedirectView("/paste/" + outputModel.getId(), true);
  }

  @GetMapping
  public String pasteListView(
    Model model,
    @RequestParam("page") Optional<Integer> page,
    @RequestParam("size") Optional<Integer> size) {

    int currentPage = page.orElse(1);
    int pageSize = size.orElse(10);

    Page<PasteListModel> pastePage = pasteService.getPastes(PageRequest.of(currentPage - 1, pageSize, Sort.by(Sort.Direction.DESC, "createdAt")));

    model.addAttribute("pastePage", pastePage);

    int totalPages = pastePage.getTotalPages();

    if (totalPages > 0) {
      List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
        .boxed()
        .collect(Collectors.toList());
      model.addAttribute("pageNumbers", pageNumbers);
    }

    return "paste_list";
  }

  private UUID parseUUIDSilently(String uuid) {
    try {
      return UUID.fromString(uuid);
    } catch (Exception e) {
      return null;
    }
  }
}
