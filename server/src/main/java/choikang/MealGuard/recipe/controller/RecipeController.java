package choikang.MealGuard.recipe.controller;

import choikang.MealGuard.dto.MultiResponseDto;
import choikang.MealGuard.recipe.entity.Recipe;
import choikang.MealGuard.recipe.mapper.RecipeMapper;
import choikang.MealGuard.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;
    private final RecipeMapper mapper;

    @GetMapping
    public ResponseEntity getRecipes(@RequestParam int page,@RequestParam int size){
        Page<Recipe> recipePage = recipeService.findRecipes(page, size);
        List<Recipe> recipes = recipePage.getContent();

        return new ResponseEntity<>(new MultiResponseDto<>(mapper.recipeToListResponse(recipes),recipePage), HttpStatus.OK);
    }

    @GetMapping("/{recipe-id}")
    public ResponseEntity getRecipe(@PathVariable("recipe-id") long recipeId){
        Recipe recipe = recipeService.findRecipe(recipeId);

        return new ResponseEntity<>(mapper.recipeToResponse(recipe),HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity getRecipe(@RequestParam(required = false) String name,@RequestParam int page,@RequestParam int size){
        Page<Recipe> recipePage = recipeService.findRecipesBySearch(name,page,size);

        List<Recipe> recipes = recipePage.getContent();

        return new ResponseEntity<>(new MultiResponseDto<>(mapper.recipeToListResponse(recipes),recipePage), HttpStatus.OK);
    }

    // 좋아요 하기
    @PostMapping("/{recipe-id}/favorite")
    public ResponseEntity<String> postFavorite(@PathVariable("recipe-id") long recipeId , @RequestHeader("Authorization") String token) {
        recipeService.createFavorite(recipeId,token);

        return ResponseEntity.ok("좋아요");
    }

    //좋아요 취소
    @DeleteMapping("/{recipe-id}/favorite")
    public ResponseEntity<String> deleteFavorite(@PathVariable("recipe-id") long recipeId , @RequestHeader("Authorization") String token) {
        recipeService.cancleFavorite(recipeId,token);

        return ResponseEntity.ok("좋아요 취소");
    }
}
