package de.krillonline.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.krillonline.model.SudokuCell;
import de.krillonline.model.SudokuField;

@Controller
public class SudokuController {
	
	@GetMapping("/")
	public String getInitForm(Model model) {
		model.addAttribute("size", new Integer(0));
		return "index";
	}
	
	@PostMapping("/fieldinputform")
	public String createFieldInputForm(Model model, @RequestParam Integer size) {
		
		int rootOfSize = (int) Math.sqrt(size); 
		if ((rootOfSize * rootOfSize) != size) {
			model.addAttribute("size", size);
			model.addAttribute("message", "Die Größe muss quadratisch sein! Bitte korrigieren!");
			return "index";
		}
		SudokuField.setSize(size);
		SudokuField field = new SudokuField(size);
		model.addAttribute("field", field);
		return "field";		
	}
	
	@PostMapping("/solutions")
	public String showSolutions(Model model, 
			@ModelAttribute(name="field") SudokuField field) {		

		if (! field.fieldIsValid()) {
			model.addAttribute("field", field);
			model.addAttribute("message", "Das eingebene Startfeld ist ungültig! Bitte korrigieren!");
			return "field";
		}

		markInitialFields(field);
				
		field.findSolutions(field.getCells());
		
		model.addAttribute("field", field);
		model.addAttribute("maxloesungenerreicht", field.getSolutions().size() == SudokuField.MAXSOLUTIONS);
				
		return "solution";		
	}

	private void markInitialFields(SudokuField field) {
		for (int row = 0; row < field.getCells().length; row++) {
			for (int col = 0; col < field.getCells()[row].length; col++) {
				SudokuCell cell = field.getCells()[row][col];
				if (cell.getValue() > 0) {
					cell.setStartValue(true);
				}
			}
		}
	}
	
}
