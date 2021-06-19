package pl.igormaculewicz.pastex.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PasteInputModel {

  private String title;
  private String content;
  private String password;

}
